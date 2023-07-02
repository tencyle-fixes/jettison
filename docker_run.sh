#!/bin/bash

cwd="$(pwd)"

# Default values - Once done developing, insert to parameters final values for future runs
source_code="$cwd"
default_command="mvn clean install"
image="tencyle/java17-builder:mvn3.9.2-1.4.0"
m2_repository_cache="$HOME/tmp/$(basename "$cwd")"

while [[ -n "$1" ]]; do

    if [[ "$1" == "-h" || "$1" == "--help" ]]; then
        echo "Usage: ./docker_run.sh [OPTIONS] <maven command>"
        echo "Default maven command - mvn clean install"
        echo "Default image tencyle/java17-builder:mvn3.9.2-1.4.0"
        echo "Options:"
        echo "  -h, --help                 Display this help message"
        echo "  -b, --bash                 Run the container with bash as the entry point, does not run maven command"
        echo "  -i, --image  <image name>  Docker image to use"
        echo "  -m, --m2    <path>         Path to local m2 repository - mandatory option. Will create m2 repository under this path."
        echo "  -s, --source <path>        Path to source code to be mounted to container."
        exit 0
    fi

    if [[ "$1" == "-b" || "$1" == "--bash" ]]; then
        run_bash=1
        shift
        continue
    fi

    if [[ "$1" == "-i" || "$1"  == "--image" ]]; then
        image="$2"
        shift
        shift
        continue
    fi

    if [[ "$1" == "-m" || "$1" == "--m2" ]]; then
        m2_repository_cache="$2"
        shift
        shift
        continue
    fi

    if [[ "$1" == "-s" || "$1" == "--source" ]]; then
        source_code="$2"
        shift
        shift
        continue
    fi

    command="$command $1"
    shift

done

# Check which command to run
if [[ -n "$run_bash" ]]; then
  command="bash"
elif [[ -z "$command" ]]; then
  command="$default_command"
fi

# Creating local m2 for project,
mkdir -p "$m2_repository_cache"/m2-repository

# Running build and tests
printf "[+] Running container with:\n"
printf "\t[+] Image: %s\n" "$image"
printf "\t[+] Source code: %s\n" "$source_code"
printf "\t[+] m2-repository: %s\n" "$m2_repository_cache"
printf "\t[+] Command: %s\n" "$command"

docker run -it \
       -v "$source_code":/code \
       -v "$m2_repository_cache"/m2-repository:/root/.m2/repository \
       "$image" \
       $command