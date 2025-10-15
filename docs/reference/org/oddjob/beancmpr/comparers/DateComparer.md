[HOME](../../../../README.md)
# beancmpr:date-comparer

A Comparer that will allow a comparison between
two Java dates with a millisecond tolerance.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [dateFormat](#propertydateformat) | The format for the date and time when reporting a date difference. | 
| [tolerance](#propertytolerance) | The difference as a number of milliseconds that can exist between two dates for them still to be considered equal. | 
| [type](#propertytype) | The base class this is a comparer for. | 


### Property Detail
#### dateFormat <a name="propertydateformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The format for the date and time when reporting a
date difference.

#### tolerance <a name="propertytolerance"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Default to 0.</td></tr>
</table>

The difference as a number of milliseconds that
can exist between two dates for them still to be considered equal.

#### type <a name="propertytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>

The base class this is a comparer for.
Used internally.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
