[HOME](../../../../README.md)
# beancmpr:text-comparer

A comparer for text. This comparer allows a
comparison ignoring whitespace and or case.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [ignoreCase](#propertyignorecase) | Ignore the case of the text during a comparison when true. | 
| [ignoreWhitespace](#propertyignorewhitespace) | Ignore leading or trailing whitespace during a comparison when true. | 
| [type](#propertytype) | The base class this is a comparer for. | 


### Property Detail
#### ignoreCase <a name="propertyignorecase"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Default to false.</td></tr>
</table>

Ignore the case of the text during a comparison
when true.

#### ignoreWhitespace <a name="propertyignorewhitespace"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Default to false.</td></tr>
</table>

Ignore leading or trailing whitespace during a
comparison when true.

#### type <a name="propertytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>

The base class this is a comparer for.
Used internally.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
