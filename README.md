# se301-project

## Directory Structure

```
src/
├── main/
│   └── java/
│       └── se301/
│           └── project/
│               
└── test/
    └── java/
        └── se301/
            └── project/
```

## Cloning the Project

To clone the project, run the following command:

```bash
git clone https://github.com/kaixuantan/se301-proj.git
cd se301-project
```

## Building the Project

To build the project, run the following command in the root directory of the project:

```bash
mvn clean install
```

## Running the Project

To run the project, execute the `Main` class. You can do this from the command line with the following command:

```bash
mvn exec:java -Dexec.mainClass="se301.project.Main" -Dexec.args="good 50"
```

## Running Tests

To run the tests, run the following command in the root directory of the project:

```bash
mvn clean test
```