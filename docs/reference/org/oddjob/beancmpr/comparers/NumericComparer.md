[HOME](../../../../README.md)
# beancmpr:numeric-comparer

A Comparer for numbers that supports tolerances and provides
the comparison as a difference between the numbers and as a percentage
change between numbers.


The change is considered as going from x to y. If x is 200 and y is 190, the
delta is -10, and the percentage change is 5%.


Two numbers are only considered different if their delta is more
the given tolerance or more. The tolerance may be specified as either a
delta tolerance number that two numbers must differ more than,
or a minimum percentage change that the two number must exceed.


The comparison is only unequal if the difference between the two numbers
is greater than both tolerances. Both tolerances default to zero.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [deltaFormat](#propertydeltaformat) | The decimal format to use for reporting a difference. | 
| [deltaTolerance](#propertydeltatolerance) | The absolute difference to allow for two numbers to still be considered equal. | 
| [percentageFormat](#propertypercentageformat) | The decimal format to use for reporting a percentage difference. | 
| [percentageTolerance](#propertypercentagetolerance) | The difference specified as percentage to allow for two numbers to still be considered equal. | 
| [twoNaNsEqual](#propertytwonansequal) | Treat two Not a Numbers as being equal when true. | 
| [type](#propertytype) | The base class this is a comparer for. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Formatting the result of a numeric compare. |


### Property Detail
#### deltaFormat <a name="propertydeltaformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The decimal format to use for reporting a
difference.

#### deltaTolerance <a name="propertydeltatolerance"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to 0.</td></tr>
</table>

The absolute difference to allow for two numbers
to still be considered equal.

#### percentageFormat <a name="propertypercentageformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The decimal format to use for reporting a
percentage difference.

#### percentageTolerance <a name="propertypercentagetolerance"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to 0.</td></tr>
</table>

The difference specified as percentage to allow
for two numbers to still be considered equal.

#### twoNaNsEqual <a name="propertytwonansequal"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

Treat two Not a Numbers as being equal when true.

#### type <a name="propertytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>Read Only</td></tr>
</table>

The base class this is a comparer for.
Used internally.


### Examples
#### Example 1 <a name="example1"></a>

Formatting the result of a numeric compare. Note that we use
JavaScript expressions to configure the numbers because Oddjob
would default to Strings as the value of configuration attribute.
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <value value="#{4.25}"/>
                    </inX>
                    <inY>
                        <value value="#{5.01}"/>
                    </inY>
                    <comparer>
                        <beancmpr:bean-comparer>
                            <comparersByType>
                                <beancmpr:comparers-by-type>
                                    <comparers>
                                        <beancmpr:numeric-comparer deltaTolerance="0.5" deltaFormat="0.1" percentageFormat="0" xmlns:beancmpr="oddjob:beancmpr"/>
                                    </comparers>
                                </beancmpr:comparers-by-type>
                            </comparersByType>
                        </beancmpr:bean-comparer>
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

The example creates the following output:
```
matchResultType  xValue  yValue  valueComparison
---------------  ------  ------  ---------------
NOT_EQUAL        4.25    5.01    1.1 (18%)
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
