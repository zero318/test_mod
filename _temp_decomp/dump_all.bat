@echo off

for %%g in (.\*.class) do (
	if not exist %%~ng.dump (
		javap_test.bat -v -constants -l -s -p -c %%g>%%~ng.dump
	)
)