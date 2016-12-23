
if "%1%"=="build"(spm build)
else(
	md "sea-modules/qishi/web/1.0.0"
	xcopy dist\*.*  sea-modules\qishi\web\1.0.0\ /s /e
	echo   " deploy to seajs-modules/qishi/web/1.0.0"
)