[HOME](../../../README.md)
# didocmpr:continuous-comparer

Creates a service that continuously compares two
streams of data.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [XMissingCount](#propertyxmissingcount) | The number of items missing from the first source. | 
| [YMissingCount](#propertyymissingcount) | The number of items missing from the second source. | 
| [breaksCount](#propertybreakscount) | The number of differences. | 
| [comparedCount](#propertycomparedcount) | The total number of items compared. | 
| [comparersByName](#propertycomparersbyname) | Comparers for comparing the field data of the Generic Data defined by the name of the field. | 
| [comparersByType](#propertycomparersbytype) | Comparers for comparing the field data. | 
| [differentCount](#propertydifferentcount) | The number of items different. | 
| [keys](#propertykeys) | The key field names. | 
| [matchedCount](#propertymatchedcount) | The number of items matched. | 
| [name](#propertyname) | The name of the job as seen in Oddjob. | 
| [others](#propertyothers) | Other field names. | 
| [results](#propertyresults) | Something to handle results. | 
| [schema](#propertyschema) | The schema, if known in advance. | 
| [sourceStrategy](#propertysourcestrategy) | The strategy. | 
| [to](#propertyto) | A destination for results that create beans. | 
| [tolerance](#propertytolerance) | The tolerance to be used. | 
| [values](#propertyvalues) | The value field names. | 
| [x](#propertyx) | Provides a consumer to accept X data. | 
| [y](#propertyy) | Provides a consumer to accept Y data. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Compare two streams of data. |


### Property Detail
#### XMissingCount <a name="propertyxmissingcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The number of items missing from the first source.

#### YMissingCount <a name="propertyymissingcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The number of items missing from the second source.

#### breaksCount <a name="propertybreakscount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The number of differences. This is the sum of the
missing and the different.

#### comparedCount <a name="propertycomparedcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The total number of items compared.

#### comparersByName <a name="propertycomparersbyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers for comparing the field data of the
Generic Data defined by the name of the field. This property is most
often set with a [beancmpr:comparers-by-name](../../../org/oddjob/beancmpr/composite/ComparersByNameType.md).

#### comparersByType <a name="propertycomparersbytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers for comparing the field data.
These comparers will override any other comparers defined
in the comparer hierarchy for their type. This property is most
often set with a [beancmpr:comparers-by-type](../../../org/oddjob/beancmpr/composite/ComparersByTypeList.md).

#### differentCount <a name="propertydifferentcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The number of items different.

#### keys <a name="propertykeys"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The key field names. The key
properties decided if another data item is missing or not.

#### matchedCount <a name="propertymatchedcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

The number of items matched.

#### name <a name="propertyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The name of the job as seen in Oddjob.

#### others <a name="propertyothers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Other field names. Other fields
may be of interest on reports but take no part in the matching
process.

#### results <a name="propertyresults"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Something to handle results. Typically, a
`beancmpr.dido.results.GenericDataResultHandler`.

#### schema <a name="propertyschema"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No, it will be taken from the first item of data.</td></tr>
</table>

The schema, if known in advance.

#### sourceStrategy <a name="propertysourcestrategy"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to ONE_FOR_ONE</td></tr>
</table>

The strategy. See [didocmpr:continuous-strategy](../../../org/oddjob/beancmpr/continuous/SourceStrategies.md) for a
means of configuration.

#### to <a name="propertyto"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

A destination for results that create beans. This
allows this job to play with Oddjob's Bean Bus Framework.

#### tolerance <a name="propertytolerance"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to PT01S (1 second).</td></tr>
</table>

The tolerance to be used. This is a duration, Oddjob
provides a conversion from java's [java.time.Duration](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/Duration.html) text format.
How it is used depends on the
strategy, but missing data is generally reported only after the tolerance period
has expired.

#### values <a name="propertyvalues"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The value field names. The value properties
decide if two data items match when their keys match.

#### x <a name="propertyx"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

Provides a consumer to accept X data.

#### y <a name="propertyy"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read only.</td></tr>
</table>

Provides a consumer to accept Y data.


### Examples
#### Example 1 <a name="example1"></a>

Compare two streams of data.
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob id="oddjob">
    <job>
        <sequential>
            <jobs>
                <properties>
                    <values>
                        <value key="compare.time.tolerance" value="PT0.2S"/>
                        <value key="stop.after.interval" value="00:00:02"/>
                    </values>
                </properties>
                <bus:bus id="bus" xmlns:bus="oddjob:beanbus">
                    <of>
                        <didocmpr:continuous-comparer id="compare" keys="Fruit,Colour" others="Comments" tolerance="${compare.time.tolerance}" values="Qty,Price" xmlns:didocmpr="oddjob:didocmpr">
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                            <sourceStrategy>
                                <didocmpr:continuous-strategy strategy="MANY_FOR_MANY"/>
                            </sourceStrategy>
                        </didocmpr:continuous-comparer>
                        <bus:collect id="results">
                            <to>
                                <stdout/>
                            </to>
                        </bus:collect>
                        <dido:data-out xmlns:dido="oddjob:dido">
                            <how>
                                <dido:table/>
                            </how>
                            <to>
                                <stdout/>
                            </to>
                        </dido:data-out>
                    </of>
                </bus:bus>
                <parallel>
                    <jobs>
                        <dido:play filesPrefix="x-" xmlns:dido="oddjob:dido">
                            <dir>
                                <file file="${oddjob.dir}/data"/>
                            </dir>
                            <to>
                                <value value="${compare.x}"/>
                            </to>
                        </dido:play>
                        <dido:play filesPrefix="y-" xmlns:dido="oddjob:dido">
                            <dir>
                                <file file="${oddjob.dir}/data"/>
                            </dir>
                            <to>
                                <value value="${compare.y}"/>
                            </to>
                        </dido:play>
                    </jobs>
                </parallel>
                <scheduling:timer xmlns:scheduling="http://rgordon.co.uk/oddjob/scheduling">
                    <schedule>
                        <schedules:count count="1" xmlns:schedules="http://rgordon.co.uk/oddjob/schedules">
                            <refinement>
                                <schedules:after>
                                    <schedule>
                                        <schedules:interval interval="${stop.after.interval}"/>
                                    </schedule>
                                </schedules:after>
                            </refinement>
                        </schedules:count>
                    </schedule>
                    <job>
                        <stop job="${bus}"/>
                    </job>
                </scheduling:timer>
            </jobs>
        </sequential>
    </job>
</oddjob>
```

The output is:
```
MatchType|Fruit |Colour|X_Qty|Y_Qty|Qty_|X_Price|Y_Price|Price_       |X_Comments|Y_Comments
---------+------+------+-----+-----+----+-------+-------+-------------+----------+----------
EQUAL    |apple |red   |    5|    5|    |   22.4|   22.4|             |crunchy   |crunchy
NOT_EQUAL|apple |green |    6|    7|6<>7|   23.2|   23.2|             |crisp     |crisp
EQUAL    |banana|yellow|    3|    3|    |   46.4|   46.4|             |bent      |bent
NOT_EQUAL|orange|orange|    2|    2|    |   23.5|   57.2|33.7 (143.4%)|healthy   |healthy
Y_MISSING|pear  |green |    8|     |    |   37.0|       |             |shapely   |
X_MISSING|pear  |gr    |     |    8|    |       |   37.0|             |          |shapely
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
