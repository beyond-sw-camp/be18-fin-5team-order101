# Python 서버 (Poetry 환경 가이드)

이 문서는 `python-server/` 프로젝트의 Python 서버 환경을 Poetry로 설정하고 실행하는 방법을 설명합니다.
Poetry는 의존성, 버전, 가상환경을 통합적으로 관리하기 위한 도구입니다.

---

## 1. Poetry란?

Poetry는 Python 프로젝트의 의존성, 버전, 가상환경, 빌드를 통합적으로 관리하는 도구입니다.
기존의 `pip + requirements.txt + venv` 조합을 대체하며, 모든 개발 환경을 통일된 방식으로 유지할 수 있습니다.

---

## 2. 프로젝트 구조

```
python-server/
├─ pyproject.toml          # Poetry 설정 파일
├─ poetry.lock             # 의존성 버전 잠금 파일
├─ .gitignore              # Git 제외 설정
└─ app/
   ├─ main.py
   ├─ routes/
   ├─ services/
   ├─ models/
   └─ utils/
```

---

## 3. Poetry 설치

### Windows

```bash
pip install poetry
```

### macOS

Homebrew 사용 시:

```bash
brew install poetry
```

Homebrew를 사용하지 않는 경우:

```bash
curl -sSL https://install.python-poetry.org | python3 -
```

설치 확인:

```bash
poetry --version
```

---

## 4. 프로젝트 의존성 설치

프로젝트 루트(`python-server/`)에서 실행합니다:

```bash
poetry install
```

이 명령은 다음을 수행합니다:

* `pyproject.toml` 및 `poetry.lock` 파일을 기준으로 의존성 설치
* `.venv` 가상환경 자동 생성
* 모든 팀원이 동일한 버전의 환경을 사용할 수 있도록 보장

---

## 5. 가상환경 진입 및 실행

### 5.1 가상환경 진입

| OS      | 명령어            |
| ------- | -------------- |
| Windows | `poetry shell` |
| macOS   | `poetry shell` |

### 5.2 가상환경 정보 확인

```bash
poetry env info
```

### 5.3 FastAPI 서버 실행

루트 디렉토리(`python-server/`)에서 실행합니다:

| OS      | 명령어                                        |
| ------- | ------------------------------------------ |
| Windows | `poetry run uvicorn app.main:app --reload` |
| macOS   | `poetry run uvicorn app.main:app --reload` |

---

## 6. 의존성 관리 명령어

| 목적          | 명령어                                   | 설명                 |
| ----------- | ------------------------------------- | ------------------ |
| 패키지 추가      | `poetry add fastapi`                  | 운영 환경 의존성 추가       |
| 개발용 패키지 추가  | `poetry add --group dev pytest black` | 개발 전용 의존성 추가       |
| 패키지 제거      | `poetry remove requests`              | 불필요한 패키지 제거        |
| 특정 패키지 업데이트 | `poetry update fastapi`               | 지정한 패키지만 갱신        |
| 전체 업데이트     | `poetry update`                       | 모든 패키지 최신화 (주의 필요) |

---

## 7. 설치된 패키지 확인

```bash
poetry show
```

설치된 모든 패키지와 버전을 확인할 수 있습니다.

---

## 8. 환경 변수 관리

`.env` 파일은 Git에 커밋하지 않습니다. 대신 `.env.example` 파일로 키 이름만 공유합니다.

예시:

```
DATABASE_URL=
SECRET_KEY=
```

로컬 환경에서는 다음 명령으로 복사합니다:

```bash
cp .env.example .env
```

---

## 9. 가상환경 경로 고정

팀 내 환경 통일을 위해 가상환경을 프로젝트 내부(`python-server/.venv/`)에 생성하도록 설정합니다.

```bash
poetry config virtualenvs.in-project true
poetry install
```

이 명령을 실행하면 `.venv/` 폴더가 `python-server/` 내부에 생성됩니다.
`.gitignore`에 `.venv/`가 반드시 포함되어야 합니다.

---

## 10. VSCode 연동

1. 명령 팔레트 실행 (`Ctrl+Shift+P` 또는 macOS는 `Cmd+Shift+P`)
2. **Python: Select Interpreter** 선택
3. Poetry 가상환경 경로 선택:

   * macOS: `python-server/.venv/bin/python`
   * Windows: `python-server/.venv/Scripts/python.exe`

---

## 11. 개발용 도구 추가 (선택)

코드 포맷터 및 테스트 도구를 설치합니다:

```bash
poetry add --group dev black isort flake8 pytest
```

코드 포맷팅 실행:

```bash
poetry run black app/
```

테스트 실행:

```bash
poetry run pytest
```

---

## 12. 단축 실행 명령 추가 (선택)

`pyproject.toml`에 다음 내용을 추가합니다:

```toml
[tool.poetry.scripts]
dev = "uvicorn app.main:app"
```

이후 다음 명령으로 실행 가능합니다:

```bash
poetry run dev --reload
```

---

## 13. 자주 사용하는 명령어 요약

| 작업         | 명령어                                         |
| ---------- | ------------------------------------------- |
| 의존성 설치     | `poetry install`                            |
| 가상환경 진입    | `poetry shell`                              |
| 서버 실행      | `poetry run uvicorn app.main:app --reload`  |
| 패키지 추가     | `poetry add <패키지명>`                         |
| 개발용 패키지 추가 | `poetry add --group dev <패키지명>`             |
| 패키지 제거     | `poetry remove <패키지명>`                      |
| 가상환경 정보 확인 | `poetry env info`                           |
| 가상환경 경로 고정 | `poetry config virtualenvs.in-project true` |

---

## 14. 주의사항

* `pip install` 명령은 사용하지 않습니다.
  모든 설치는 `poetry add` 또는 `poetry install`로 수행해야 합니다.
* `.venv`, `.env`, `__pycache__/` 폴더는 절대 Git에 커밋하지 않습니다.
* `pyproject.toml`과 `poetry.lock`은 반드시 커밋해야 합니다. (의존성 버전 고정)
* 팀원은 `poetry install` 한 줄로 동일한 환경을 재현할 수 있습니다.
