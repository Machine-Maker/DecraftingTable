if [[ -z "$1" ]]; then
  echo "Missing Jar file parameter"
  exit 1
elif [[ -z "$2" ]]; then
  echo "Missing output directory parameter"
  exit 1
fi

cp "$1" "$2"
echo "Successfully copied $1 to $2"