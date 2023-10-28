@echo off

for %%g in (.\*.class) do (
	if not exist %%~ng.jasm (
		"F:\My Programs Expansion\Java\bytecode_tools\jasm-0.7.0\jasm-0.7.0\bin\jasm_run.bat" -d %%g
	)
)