[HOME](../../../README.md)
# didocmpr:collection-comparer

Compares two Iterables of Dido Data. Iterables will
typically be lists or sets.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparersByName](#propertycomparersbyname) | Comparers for comparing the field data of the Generic Data defined by the name of the field. | 
| [comparersByType](#propertycomparersbytype) | Comparers for comparing the field data. | 
| [keys](#propertykeys) | The key field names. | 
| [others](#propertyothers) | Other field names. | 
| [sorted](#propertysorted) | Are the collections sorted. | 
| [values](#propertyvalues) | The value field names. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Comparing two lists of Dido Data. |
| [Example 2](#example2) | Using the `comparersByType` property to configure the comparers used for the comparison. |
| [Example 3](#example3) | Using the `comparersByName` property to configure the comparers used for the comparison. |


### Property Detail
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

#### keys <a name="propertykeys"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The key field names. The key
properties decided if another data item is missing or not.

#### others <a name="propertyothers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Other field names. Other fields
may be of interest on reports but take no part in the matching
process.

#### sorted <a name="propertysorted"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

Are the collections sorted. If collections are sorted
then key comparison will be much quicker.

#### values <a name="propertyvalues"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The value field names. The value properties
decide if two data items match when their keys match.


### Examples
#### Example 1 <a name="example1"></a>

Comparing two lists of Dido Data. The Data is created from
parsing CSV files. The comparison results are also created as Dido Data and
are written as a Text Table. As they are Dido Data they could have captured as
a CSV, JSON or written to a database.
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <variables id="vars">
                    <schema>
                        <dido:schema xmlns:dido="oddjob:dido">
                            <of>
                                <dido:field name="Fruit" type="java.lang.String"/>
                                <dido:field name="Colour" type="java.lang.String"/>
                                <dido:field name="Qty" type="int"/>
                                <dido:field name="Price" type="double"/>
                                <dido:field name="Comments" type="java.lang.String"/>
                            </of>
                        </dido:schema>
                    </schema>
                </variables>
                <dido:data-in id="data1" name="Data 1" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[apple,red,5,22.4,crunchy
apple,green,6,23.2,crisp
banana,yellow,3,46.4,bent
orange,orange,2,23.5,healthy
pear,green,8,37.0,shapely]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <dido:data-in id="data2" name="Data 2" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[apple,red,5,22.4,crunchy
apple,green,7,23.2,crisp
banana,yellow,3,46.4,bent
orange,orange,2,57.2,healthy
pear,gr,8,37.0,shapely]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <bus:bus id="bus" xmlns:bus="oddjob:beanbus">
                    <of>
                        <beancmpr:compare id="comparison" xmlns:beancmpr="oddjob:beancmpr">
                            <inX>
                                <value value="${data1.to}"/>
                            </inX>
                            <inY>
                                <value value="${data2.to}"/>
                            </inY>
                            <comparer>
                                <didocmpr:collection-comparer keys="Fruit,Colour" others="Comments" sorted="false" values="Qty, Price" xmlns:didocmpr="oddjob:didocmpr"/>
                            </comparer>
                            <results>
                                <didocmpr:data-results xmlns:didocmpr="oddjob:didocmpr"/>
                            </results>
                        </beancmpr:compare>
                        <bus:collect id="results"/>
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
            </jobs>
        </sequential>
    </job>
</oddjob>
```

The output is:
```
MatchType|Fruit |Colour|X_Qty|Y_Qty|Qty_|X_Price|Y_Price|Price_       |X_Comments|Y_Comments
---------+------+------+-----+-----+----+-------+-------+-------------+----------+----------
NOT_EQUAL|apple |green |    6|    7|6<>7|   23.2|   23.2|             |crisp     |crisp
EQUAL    |apple |red   |    5|    5|    |   22.4|   22.4|             |crunchy   |crunchy
EQUAL    |banana|yellow|    3|    3|    |   46.4|   46.4|             |bent      |bent
NOT_EQUAL|orange|orange|    2|    2|    |   23.5|   57.2|33.7 (143.4%)|healthy   |healthy
X_MISSING|pear  |gr    |     |    8|    |       |   37.0|             |          |shapely
Y_MISSING|pear  |green |    8|     |    |   37.0|       |             |shapely   |
```


#### Example 2 <a name="example2"></a>

Using the `comparersByType` property to configure
the comparers used for the comparison. Note that we specify a comparer
for all Numeric types, but then override this with a specialisation for
Integers. We see that despite differences in the inputs, Beancmpr now
treats the two items as equal.
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <variables id="vars">
                    <schema>
                        <dido:schema xmlns:dido="oddjob:dido">
                            <of>
                                <dido:field name="Fruit" type="java.lang.String"/>
                                <dido:field name="Qty" type="int"/>
                                <dido:field name="Price" type="double"/>
                            </of>
                        </dido:schema>
                    </schema>
                </variables>
                <dido:data-in id="data1" name="Data 1" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[apple,25,22.7]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <dido:data-in id="data2" name="Data 2" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[APPLE,26,22.4]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <bus:bus id="bus" xmlns:bus="oddjob:beanbus">
                    <of>
                        <beancmpr:compare id="comparison" xmlns:beancmpr="oddjob:beancmpr">
                            <inX>
                                <value value="${data1.to}"/>
                            </inX>
                            <inY>
                                <value value="${data2.to}"/>
                            </inY>
                            <comparer>
                                <didocmpr:collection-comparer values="Fruit, Qty, Price" xmlns:didocmpr="oddjob:didocmpr">
                                    <comparersByType>
                                        <beancmpr:comparers-by-type>
                                            <comparers>
                                                <beancmpr:numeric-comparer deltaTolerance="0.5"/>
                                                <beancmpr:text-comparer ignoreCase="true"/>
                                            </comparers>
                                            <specialisations>
                                                <beancmpr:numeric-comparer key="java.lang.Integer" percentageTolerance="10"/>
                                            </specialisations>
                                        </beancmpr:comparers-by-type>
                                    </comparersByType>
                                </didocmpr:collection-comparer>
                            </comparer>
                            <results>
                                <didocmpr:data-results xmlns:didocmpr="oddjob:didocmpr"/>
                            </results>
                        </beancmpr:compare>
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
            </jobs>
        </sequential>
    </job>
</oddjob>
```

The output is:
```
MatchType|X_Fruit|Y_Fruit|Fruit_|X_Qty|Y_Qty|Qty_|X_Price|Y_Price|Price_
---------+-------+-------+------+-----+-----+----+-------+-------+------
EQUAL    |apple  |APPLE  |      |   25|   26|    |   22.7|   22.4|
```


#### Example 3 <a name="example3"></a>

Using the `comparersByName` property to configure
the comparers used for the comparison. The name of the property is
given as the key. It must exactly match the incoming field name. If it
doesn't, Beancmpr will silently fall back on the default comparer.
As above, we see that despite differences in the inputs, Beancmpr
treats the two items as equal.
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <variables id="vars">
                    <schema>
                        <dido:schema xmlns:dido="oddjob:dido">
                            <of>
                                <dido:field name="Fruit" type="java.lang.String"/>
                                <dido:field name="Qty" type="int"/>
                                <dido:field name="Price" type="double"/>
                            </of>
                        </dido:schema>
                    </schema>
                </variables>
                <dido:data-in id="data1" name="Data 1" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[apple,25,22.7]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <dido:data-in id="data2" name="Data 2" xmlns:dido="oddjob:dido">
                    <how>
                        <dido:csv>
                            <schema>
                                <value value="${vars.schema}"/>
                            </schema>
                        </dido:csv>
                    </how>
                    <from>
                        <buffer><![CDATA[APPLE,26,22.4]]></buffer>
                    </from>
                    <to>
                        <list/>
                    </to>
                </dido:data-in>
                <bus:bus id="bus" xmlns:bus="oddjob:beanbus">
                    <of>
                        <beancmpr:compare id="comparison" xmlns:beancmpr="oddjob:beancmpr">
                            <inX>
                                <value value="${data1.to}"/>
                            </inX>
                            <inY>
                                <value value="${data2.to}"/>
                            </inY>
                            <comparer>
                                <didocmpr:collection-comparer values="Fruit, Qty, Price" xmlns:didocmpr="oddjob:didocmpr">
                                    <comparersByName>
                                        <beancmpr:comparers-by-name>
                                            <comparers>
                                                <beancmpr:numeric-comparer key="Qty" percentageTolerance="10"/>
                                                <beancmpr:numeric-comparer deltaTolerance="0.5" key="Price"/>
                                                <beancmpr:text-comparer ignoreCase="true" key="Fruit"/>
                                            </comparers>
                                        </beancmpr:comparers-by-name>
                                    </comparersByName>
                                </didocmpr:collection-comparer>
                            </comparer>
                            <results>
                                <didocmpr:data-results xmlns:didocmpr="oddjob:didocmpr"/>
                            </results>
                        </beancmpr:compare>
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
            </jobs>
        </sequential>
    </job>
</oddjob>
```

The output is:
```
MatchType|X_Fruit|Y_Fruit|Fruit_|X_Qty|Y_Qty|Qty_|X_Price|Y_Price|Price_
---------+-------+-------+------+-----+-----+----+-------+-------+------
EQUAL    |apple  |APPLE  |      |   25|   26|    |   22.7|   22.4|
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
