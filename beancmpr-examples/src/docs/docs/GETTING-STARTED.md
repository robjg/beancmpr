Getting Started
===============

Beancmpr requires Java 21. It is in Maven, however it is intended to be used with
[Oddjob](https://github.com/robjg/oddjob) and so requires a few dependencies to launch.

For an example POM needed to launch Beancmpr in Oddjob see [The Examples POM](../beancmpr-examples/pom.xml).

The pom also contains a target to run the example comparison from the [README](../README.md).

Assuming you have Maven installed, clone this repo, and from a command prompt change directory to 
`beancmpr-examples` and run 
```shell
mvn exec:exec@example1 -P examples 
```
You should see
```
Compared: 7
Matched: 2
Different: 2
Missing from First: 1
Missing from Second: 2
Created Full Comparison in C:\Users\rob\projects\beancmpr\beancmpr-examples\FruitComparison.xlsx
```
Oddjob is configured using XML. This is the configuration used to run this example:
{@oddjob.xml.file src/examples/CsvCompare.xml}
This will appear quite confusing at first. It might be easier to view it using Oddob's UI, 
Oddjob Explorer which can be done by running
```shell
exec:exec@oddjob-explorer-example1 -P examples
```
You will see
![Oddjob Explorer Example1 Read](images/OddjobExplorerExample1Ready.jpg)
Run the root job node from the 'Job' Menu and you will see the jobs run and hopefully complete:
![Oddjob Explorer Example1 Read](images/OddjobExplorerExample1Complete.jpg)
Here we've navigated to the Comparison Job so you can see properties of the comparison that
get written the console by the final `echo` task.

And that's a very quick guid to getting started with Beancmpr.


