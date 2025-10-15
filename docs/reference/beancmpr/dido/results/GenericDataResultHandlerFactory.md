[HOME](../../../README.md)
# didocmpr:data-results

A Result Handler that will generate results as Generic Data.
Useful for exporting results to CSV, or Excel. Although beancmpr-poi might be
better for Excel as it colours results according to match type.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparisonFieldFormat](#propertycomparisonfieldformat) | The format of the comparison fields. | 
| [ignoreMatches](#propertyignorematches) | If true then result data will not be created when a comparison results in a match. | 
| [xFieldFormat](#propertyxfieldformat) | The format of X fields. | 
| [yFieldFormat](#propertyyfieldformat) | The format of Y fields. | 


### Property Detail
#### comparisonFieldFormat <a name="propertycomparisonfieldformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to %s_.</td></tr>
</table>

The format of the comparison fields. Must contain one and only one %s which will be replaced
with the underlying field name.

#### ignoreMatches <a name="propertyignorematches"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

If true then result data will not be created
when a comparison results in a match. If false result data
for all comparisons will be created.

#### xFieldFormat <a name="propertyxfieldformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to X_%s.</td></tr>
</table>

The format of X fields. Must contain one and only one %s which will be replaced
with the underlying field name.

#### yFieldFormat <a name="propertyyfieldformat"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to Y_%s.</td></tr>
</table>

The format of Y fields. Must contain one and only one %s which will be replaced
with the underlying field name.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
