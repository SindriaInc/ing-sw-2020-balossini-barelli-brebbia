### How to launch the JAR ###

The application requires Java 14 or newer.
JavaFX is NOT required (it's bundled in the jar).

Launching the jar with no arguments will provide a list of parameters that can be used.



### Server

java -jar Santorini.jar server

Additional options:
-port NUMBER 		# Bind the specified port
-config FILE 		# Read the config from the file instead of the jar
-log-path FILE 		# Write the logs in the file instead of the default path
-deck-path FILE 	# Read the deck from the file instead of the jar

Example: java -jar Santorini.jar server -port 25565



### Client

The GUI will be launched by default.

java -jar Santorini.jar client

Additional options:
cli 				# Launch the cli
gui 				# Launch the gui (same as no option)

Example: java -jar Santorini.jar client cli
