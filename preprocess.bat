@echo off
rd /s /q .\src\main\java
pushd .\src\main\javac\
for /f usebackq %%g in (`xcopy /S /Y /R /I "yeet\*.java" "..\java\yeet\"^|find /v "File(s)"`) do (
REM cpp.exe -undef -nostdinc -P -E -o "..\java\%%g" "%%g"
REM "F:/Program Files/LLVM/bin/clang.exe" -undef -nostdinc -fms-extensions -fno-minimize-whitespace -E -C -Wno-invalid-token-paste -P -o "..\java\%%g" -std=c2x -x c "%%g"
"F:/Program Files/LLVM/bin/clang.exe" -undef -nostdinc -fms-extensions -fno-minimize-whitespace -E -Wno-invalid-token-paste -P -o "..\java\%%g" -std=c2x -x c "%%g"
for /f "usebackq tokens=3 delims= " %%h in (`findstr /B /C:"public class" "..\java\%%g" 2^>nul`) do ren "..\java\%%g" %%h%%~xg
for /f "usebackq tokens=3 delims= " %%h in (`findstr /B /C:"public @interface" "..\java\%%g" 2^>nul`) do ren "..\java\%%g" %%h%%~xg
for /f "usebackq tokens=4 delims= " %%h in (`findstr /B /C:"public final class" "..\java\%%g" 2^>nul`) do ren "..\java\%%g" %%h%%~xg
for /f "usebackq tokens=4 delims= " %%h in (`findstr /B /C:"public abstract class" "..\java\%%g" 2^>nul`) do ren "..\java\%%g" %%h%%~xg
)
popd