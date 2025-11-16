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


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
