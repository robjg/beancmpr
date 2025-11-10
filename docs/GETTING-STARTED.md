Getting Started
===============

Beancmpr requires Java 21. It is in Maven, however it is intended to be used with
[Oddjob](https://github.com/robjg/oddjob) and so requires a few dependencies to launch.

For an example POM needed to launch Beancmpr in Oddjob see the [The Example POM](beancmpr-examples/pom.xml).

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
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <cascade>
            <jobs>
                <properties>
                    <values>
                        <file file="." key="work.dir"/>
                        <file file="${work.dir}/FruitComparison.xlsx" key="comparison.file"/>
                        <file file="./data" key="data.dir"/>
                        <file file="${data.dir}/Fruit_1.csv" key="file.first"/>
                        <file file="${data.dir}/Fruit_2.csv" key="file.second"/>
                    </values>
                </properties>
                <variables id="vars">
                    <schema>
                        <dido:schema xmlns:dido="oddjob:dido">
                            <of>
                                <dido:field name="Fruit" type="java.lang.String"/>
                                <dido:field name="Quantity" type="int"/>
                                <dido:field name="Price" type="double"/>
                            </of>
                        </dido:schema>
                    </schema>
                </variables>
                <parallel name="Load CSVs In Parallel">
                    <jobs>
                        <bus:bus name="Load Frist CSV" xmlns:bus="oddjob:beanbus">
                            <of>
                                <dido:data-in xmlns:dido="oddjob:dido">
                                    <how>
                                        <dido:csv>
                                            <schema>
                                                <value value="${vars.schema}"/>
                                            </schema>
                                        </dido:csv>
                                    </how>
                                    <from>
                                        <file file="${file.first}"/>
                                    </from>
                                </dido:data-in>
                                <bus:collect id="captureFirst"/>
                            </of>
                        </bus:bus>
                        <bus:bus name="Load Second CSV" xmlns:bus="oddjob:beanbus">
                            <of>
                                <dido:data-in xmlns:dido="oddjob:dido">
                                    <how>
                                        <dido:csv>
                                            <schema>
                                                <value value="${vars.schema}"/>
                                            </schema>
                                        </dido:csv>
                                    </how>
                                    <from>
                                        <file file="${file.second}"/>
                                    </from>
                                </dido:data-in>
                                <bus:collect id="captureSecond"/>
                            </of>
                        </bus:bus>
                    </jobs>
                </parallel>
                <bus:bus name="Do Compare" xmlns:bus="oddjob:beanbus">
                    <of>
                        <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
                            <inX>
                                <value value="${captureFirst.list}"/>
                            </inX>
                            <inY>
                                <value value="${captureSecond.list}"/>
                            </inY>
                            <comparer>
                                <didocmpr:collection-comparer keys="Fruit" values="Quantity,Price" xmlns:didocmpr="oddjob:didocmpr"/>
                            </comparer>
                            <results>
                                <beancmpr:match-results/>
                            </results>
                        </beancmpr:compare>
                        <beancmpr:poi-results autoFilter="true" autoWidth="true" firstColumn="2" firstRow="2" name="Create Excel Sheet" sheetName="FruitComparison" xPrefix="First" yPrefix="Second" xmlns:beancmpr="oddjob:beancmpr">
                            <workbook>
                                <dido-poi:workbook xmlns:dido-poi="oddjob:dido:poi">
                                    <output>
                                        <file file="${comparison.file}"/>
                                    </output>
                                </dido-poi:workbook>
                            </workbook>
                        </beancmpr:poi-results>
                    </of>
                </bus:bus>
                <echo><![CDATA[Compared: ${compare.comparedCount}
Matched: ${compare.matchedCount}
Different: ${compare.differentCount}
Missing from First: ${compare.XMissingCount}
Missing from Second: ${compare.YMissingCount}
Created Full Comparison in ${comparison.file}]]></echo>
            </jobs>
        </cascade>
    </job>
</oddjob>
```

This will appear quite confusing at first. It might be easier to view it using Oddob's UI, 
Oddjob Explorer which can be done by running
```shell
exec:exec@oddjob-explorer-example1 -P examples
```
You will see
![Oddjob Explorer Example1 Read](images/OddjobExplorerExample1Ready.jpg)
Run the root job node from the 'Job' Menu and you will see the jobs run and hopefully complete:
![Oddjob Explorer Example1 Read](images/OddjobExplorerExample1Ready.jpg)
Here we've navigated to the Comparison Job so you can see properties of the comparison that
get written the console by the final `echo` task.

And that's a very quick guid to getting started with Beancmpr.


