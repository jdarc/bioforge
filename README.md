# BioForge (1.0.0)
Versatile, compact and easy to use Genetic Algorithm and Genetic Programming engine.
Simple things are beautiful things, a handful of classes constitutes the entire engine!
A minimal amount of configuration and composition to get something running!

## License
Licensed under the Apache 2.0 License.

## Requirements
*  [**Jave 14**](https://sdkman.io/) runtime for using the library and running demonstration programs.
*  [**Jave 14**](https://sdkman.io/) development kit for compilation of library and programs.
*  [**MAVEN 3.6.3**](https://maven.apache.org/) for project and dependancy management.

## Demos
Ensure you have successfully compiled the project before running the demonstrations by executing:
> mvn compile

### Circles
![](https://dl.dropboxusercontent.com/u/6389075/circles.png)
> mvn exec:exec -Dcircles

This demonstration creates a landscape of small circles and then attempts to find the largest circle that can fit amongst them without overlap. Press **R** to reset the demo or resize the window to make the environment smaller and more crowded.

### Equations
![](https://dl.dropboxusercontent.com/u/6389075/equations.png)
> mvn exec:exec -Dequations

This demonstration attempts to find the best way to arrange a formula of numbers to achieve a target value. There are two sets of numbers, a large set [25, 50, 75, 100] and a small set [1..10]. The target value is a whole number randomly chosen from the range [1..999], and the basic mathematical operators are used, namely +, -, * and /. The challenge here is to find a solution which uses **ALL** the numbers!

### Packman
![](https://dl.dropboxusercontent.com/u/6389075/packman.png)
> mvn exec:exec -Dpackman

This demonstration first generates a convex polygon and then attempts to fill this polygon with 10 non overlapping circles whilst maximizing polygon coverage. Press **R** to reset the demo and generate a new convex polygon.

### Rostering
![](https://dl.dropboxusercontent.com/u/6389075/rostering.png)
> mvn exec:exec -Drostering

This demonstration attempts to find an optimal solution to what is known as the nurse rostering problem, albeit with a vastly more simplified domain model of employees, shifts and abscense and a small handful of constraints.

### Worms
![](https://dl.dropboxusercontent.com/u/6389075/worms.png)
> mvn exec:exec -Dworms

A demonstration of Genetic Programming and emergent behaviour. This experiment attempts to evolve worm like creatures that navigate a procedurally generated 2D world, using cellular automata, avoiding obstacles whilst searching for the food.

**Minimize** the window to prevent the replay of current generations champion and thus speed up evolution cycles.

**Maximize** the window to cause replay of the current generations champion, please note that it might take a while after you maximize for the champion to replay depending on the overall fitness of that generation.

The environment is set to change every 5 minutes to ensure the creatures evolve a more holisitic survival strategy.

## Used software
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Release notes
### 1.0.0
* Initial release
