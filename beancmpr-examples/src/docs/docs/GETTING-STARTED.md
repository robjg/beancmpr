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
Expand all the jobs in the tree and you will see:

<img alt="Oddjob Explorer Example1 Ready" src="images/OddjobExplorerExample1Ready.jpg" width="600px"/>

We can now see all the stages of the comparison. They are:

- [Oddjob](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/Oddjob.md): The job that loaded the configuration thus creating all the other jobs.
  - [CascadeJob](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/state/CascadeJob.md): A job that will run the next of its children after the previous completes
    - [PropertiesJob](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/values/properties/PropertiesJob.md): A job that defines properties such as the file names for other jobs to use.
    - [Variables](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/values/VariablesJob.md): A job that defines reusable definitions for other jobs to use. Here we define the [schema](https://github.com/robjg/dido/blob/master/docs/reference/dido/oddjob/schema/SchemaBean.md) for our data.
    - [Load CSVs in Parallel](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/jobs/structural/ParallelJob.md): A job that runs its child jobs (The 2 CSV loads) at the same time. 
      - [Load First CSV](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/beanbus/bus/BasicBusService.md): Defines a data pipeline for reading the first CSV file.
        - [DataInDriver](https://github.com/robjg/dido/blob/master/docs/reference/dido/oddjob/beanbus/DataInDriver.md): Something that can load data in different formats. Here it's configured for [CSV](https://github.com/robjg/dido/blob/master/docs/reference/dido/csv/CsvDido.md).
        - [BusCollect](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/beanbus/destinations/BusCollect.md): Collect the data.
      - Load Second CSV: As above, but for the second file.
        - DataInDriver: As above, but load the second file from csv.
        - BusCollect: As above, but collect the second file's data.
    - [Do Compare](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/beanbus/bus/BasicBusService.md): Define a data
    pipeline that will pipe the results of the comparison into something that will display the results.
      - [BeanCompareJob](https://github.com/robjg/beancmpr/blob/master/docs/reference/org/oddjob/beancmpr/BeanCompareJob.md): Does the comparison using a comparer and sends results to the next component in the pipeline. 
      The comparer is a [Collection Comparer for Dido Data](https://github.com/robjg/beancmpr/blob/master/docs/reference/beancmpr/dido/beans/IterableDataComparerType.md). 
      The results produced are first [Processed](https://github.com/robjg/beancmpr/blob/master/docs/reference/org/oddjob/beancmpr/results/MatchResultHandlerFactory.md) before
      they are sent onwards.
      - [Create Excel Sheet](https://github.com/robjg/beancmpr/blob/master/docs/reference/beancmpr/poi/PoiMatchResultsService.md): Takes the 
      results and create an Excel sheet.
    - [Echo](https://github.com/robjg/oddjob/blob/master/docs/reference/org/oddjob/jobs/EchoJob.md): Display a summary of the comparison by using
    properties of the comparison job.

Run the root job node from the 'Job' Menu and you will see the jobs run and hopefully complete:

<img alt="Oddjob Explorer Example1 Complete" src="images/OddjobExplorerExample1Complete.jpg" width="600px"/>

Here we've navigated to the Comparison Job so you can see properties of the comparison that
get written the console by the final `echo` task.

And that's a very quick guid to getting started with Beancmpr.


