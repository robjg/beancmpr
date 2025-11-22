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


If either or both input number is null, the result of the compare is null.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [deltaFormat](#propertydeltaformat) | The decimal format to use for reporting a difference. | 
| [deltaTolerance](#propertydeltatolerance) | The absolute difference to allow for two numbers to still be considered equal. | 
| [percentageFormat](#propertypercentageformat) | The decimal format to use for reporting a percentage difference. | 
| [percentageTolerance](#propertypercentagetolerance) | The difference specified as percentage to allow for two numbers to still be considered equal. | 
| [twoNaNsEqual](#propertytwonansequal) | Treat two Not a Numbers as being equal when true. | 
| [type](#propertytype) | The base class this is a comparer for. | 


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


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
