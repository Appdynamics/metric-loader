This is a test application to fetch the metrics from controller.

BUILD

1. Make sure that you have java 8 installed
2. BUILD
(win)
gradlew.bat clean install
(unix)
./gradlew clean install

CONFIG
1. cd build/metric-loader
2. Rename controllers.yml.template to controllers.yml and fill the values
3. The metrics list are defined in metrics.yml


RUN

1. Run the following command from the directory `build/metric-loader`
2. java -jar metric-loader-0.0.1-SNAPSHOT.jar