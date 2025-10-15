[HOME](../../../README.md)
# beancmpr:compare

A job that takes two streams of beans and
attempts to match the beans according to their properties.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [XMissingCount](#propertyxmissingcount) |  | 
| [YMissingCount](#propertyymissingcount) |  | 
| [breaksCount](#propertybreakscount) |  | 
| [comparedCount](#propertycomparedcount) |  | 
| [comparer](#propertycomparer) |  | 
| [differentCount](#propertydifferentcount) |  | 
| [inX](#propertyinx) | First source of data. | 
| [inY](#propertyiny) | Second source of data. | 
| [matchedCount](#propertymatchedcount) |  | 
| [name](#propertyname) | The name of the job as seen in Oddjob. | 
| [results](#propertyresults) | Something to handle results. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | A simple example. |


### Property Detail
#### XMissingCount <a name="propertyxmissingcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### YMissingCount <a name="propertyymissingcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### breaksCount <a name="propertybreakscount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### comparedCount <a name="propertycomparedcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### comparer <a name="propertycomparer"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
</table>



#### differentCount <a name="propertydifferentcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### inX <a name="propertyinx"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

First source of data.

#### inY <a name="propertyiny"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

Second source of data.

#### matchedCount <a name="propertymatchedcount"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### name <a name="propertyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The name of the job as seen in Oddjob.

#### results <a name="propertyresults"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Something to handle results. Typically a
`org.oddjob.beancmpr.matchables.CompareResultsHandler`.


### Examples
#### Example 1 <a name="example1"></a>

A simple example.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <bean class="org.oddjob.beancmpr.SharedTestData" id="test-data"/>
                <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <value value="${test-data.listFruitX}"/>
                    </inX>
                    <inY>
                        <value value="${test-data.listFruitY}"/>
                    </inY>
                    <comparer>
                        <beancmpr:collection-comparer keys="id" sorted="false" values="type, quantity, colour"/>
                    </comparer>
                    <results>
                        <beancmpr:bean-results>
                            <out>
                                <list/>
                            </out>
                        </beancmpr:bean-results>
                    </results>
                </beancmpr:compare>
                <bean-report>
                    <beans>
                        <value value="${compare.results.out}"/>
                    </beans>
                </bean-report>
            </jobs>
        </sequential>
    </job>
</oddjob>
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
