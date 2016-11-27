#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

VERSION=$(git rev-parse --short HEAD)
echo -n "$VERSION" > "${DIR}/version.txt"
