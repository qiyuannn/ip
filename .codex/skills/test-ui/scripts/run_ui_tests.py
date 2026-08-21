#!/usr/bin/env python3
"""Run UI tests for the Kong chatbot from test/ui-test-plan.md."""

from __future__ import annotations

import re
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[4]
PLAN_PATH = PROJECT_ROOT / "test" / "ui-test-plan.md"
JAVA_SRC = PROJECT_ROOT / "src" / "main" / "java"


@dataclass
class TestCase:
    name: str
    aim: str
    inputs: str
    expected: str


def extract_section(body: str, heading: str) -> str:
    pattern = rf"### {re.escape(heading)}\s*```(?:text)?\n(.*?)\n```"
    match = re.search(pattern, body, re.DOTALL)
    if not match:
        raise ValueError(f"Missing section: {heading}")
    return match.group(1)


def parse_plan() -> list[TestCase]:
    text = PLAN_PATH.read_text(encoding="utf-8")
    chunks = re.split(r"(?m)^## ", text)
    cases: list[TestCase] = []

    for chunk in chunks[1:]:
        title, _, body = chunk.partition("\n")
        if not title.strip().lower().startswith("tc"):
            continue

        aim_match = re.search(r"(?m)^\*\*Aim:\*\*\s*(.+)$", body)
        if not aim_match:
            raise ValueError(f"Missing aim for {title.strip()}")

        cases.append(
            TestCase(
                name=title.strip(),
                aim=aim_match.group(1).strip(),
                inputs=extract_section(body, "Input"),
                expected=extract_section(body, "Expected Output"),
            )
        )

    if not cases:
        raise ValueError(f"No test cases found in {PLAN_PATH}")

    return cases


def compile_sources(build_dir: Path) -> None:
    sources = sorted(str(path) for path in JAVA_SRC.glob("*.java"))
    subprocess.run(["javac", "-d", str(build_dir), *sources], check=True, cwd=PROJECT_ROOT)


def run_case(build_dir: Path, test_case: TestCase) -> str:
    input_text = test_case.inputs
    if not input_text.endswith("\n"):
        input_text += "\n"

    result = subprocess.run(
        ["java", "-cp", str(build_dir), "Kong"],
        input=input_text,
        text=True,
        capture_output=True,
        cwd=PROJECT_ROOT,
        check=False,
    )

    return result.stdout + result.stderr


def assert_expected_output(actual: str, expected: str) -> str | None:
    search_from = 0
    for expected_line in expected.splitlines():
        if not expected_line.strip():
            continue
        found_at = actual.find(expected_line, search_from)
        if found_at == -1:
            return expected_line
        search_from = found_at + len(expected_line)
    return None


def print_transcript(test_case: TestCase, actual: str) -> None:
    print(f"## {test_case.name}")
    print(f"Aim: {test_case.aim}")
    print()
    print("Console input:")
    print("```text")
    print(test_case.inputs)
    print("```")
    print()
    print("Console output:")
    print("```text")
    print(actual.rstrip())
    print("```")
    print()


def main() -> int:
    try:
        test_cases = parse_plan()
        with tempfile.TemporaryDirectory(prefix="kong-ui-tests-") as temp_dir:
            build_dir = Path(temp_dir)
            compile_sources(build_dir)

            for test_case in test_cases:
                actual = run_case(build_dir, test_case)
                missing = assert_expected_output(actual, test_case.expected)
                print_transcript(test_case, actual)

                if missing is not None:
                    print("TEST FAILED")
                    print(f"Failed test case: {test_case.name}")
                    print()
                    print("Expected output snippet not found in order:")
                    print("```text")
                    print(missing)
                    print("```")
                    print()
                    print("Full expected output snippets:")
                    print("```text")
                    print(test_case.expected)
                    print("```")
                    return 1

                print("Result: PASS")
                print()

    except Exception as error:
        print(f"Unable to run UI tests: {error}", file=sys.stderr)
        return 1

    print("All UI tests passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
