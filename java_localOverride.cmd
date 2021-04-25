@REM
@REM Copyright (c) 2011-${year}, JFXtras
@REM All rights reserved.
@REM
@REM Redistribution and use in source and binary forms, with or without
@REM modification, are permitted provided that the following conditions are met:
@REM    Redistributions of source code must retain the above copyright
@REM       notice, this list of conditions and the following disclaimer.
@REM    Redistributions in binary form must reproduce the above copyright
@REM       notice, this list of conditions and the following disclaimer in the
@REM       documentation and/or other materials provided with the distribution.
@REM    Neither the name of the organization nor the
@REM       names of its contributors may be used to endorse or promote products
@REM       derived from this software without specific prior written permission.
@REM
@REM THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
@REM ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
@REM WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
@REM DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
@REM DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
@REM (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
@REM LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
@REM ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
@REM (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
@REM SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
@REM

call %~dp0\java_version.cmd
if %JAVA_VERSION%==8 set JAVA_HOME=C:\Progra~1\Java\jdk1.8.0_162
if %JAVA_VERSION%==9 set JAVA_HOME=C:\Progra~1\Java\jdk-9.0.1
if %JAVA_VERSION%==10 set JAVA_HOME=C:\Progra~1\Java\jdk-10.0.2
if %JAVA_VERSION%==11 set JAVA_HOME=C:\Progra~1\Java\jdk-11.0.1
if %JAVA_VERSION%==14 set JAVA_HOME=C:\Progra~1\Java\jdk-14.0.2+12
if %JAVA_VERSION%==15 set JAVA_HOME=C:\Progra~1\Java\jdk-15.0.1+9
%JAVA_HOME%\bin\javac -version
