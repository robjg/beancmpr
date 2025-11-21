[HOME](../../../../README.md)
# beancmpr:collection-comparer

Compares two Iterables of Java Beans. Iterables will
typically be lists or sets. If no Java Bean
properties are specified for the comparison then a comparison of the
elements is made using a comparer defined for the class of the elements,
or a default comparer if none is specified.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparersByName](#propertycomparersbyname) | Comparers for comparing the properties of the beans defined by the name of the property. | 
| [comparersByType](#propertycomparersbytype) | Comparers for comparing the properties of the beans. | 
| [keys](#propertykeys) | The key property names. | 
| [others](#propertyothers) | Other property names. | 
| [sorted](#propertysorted) | Are the collections sorted. | 
| [values](#propertyvalues) | The value property names. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | [beancmpr:array-comparer](../../../../org/oddjob/beancmpr/beans/BeanArrayComparerType.md)has an example of comparing two lists of beans. |


### Property Detail
#### comparersByName <a name="propertycomparersbyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers for comparing the properties of the
beans defined by the name of the property. This property is most
often set with a [beancmpr:comparers-by-name](../../../../org/oddjob/beancmpr/composite/ComparersByNameType.md).

#### comparersByType <a name="propertycomparersbytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Comparers for comparing the properties of the
beans. These comparers will override any other comparers defined
in the comparer hierarchy for their type. This property is most
often set with a [beancmpr:comparers-by-type](../../../../org/oddjob/beancmpr/composite/ComparersByTypeList.md).

#### keys <a name="propertykeys"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The key property names. The key
properties decided if another bean is missing or not.

#### others <a name="propertyothers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Other property names. Other properties
may be of interest on reports but take no part in the matching
process.

#### sorted <a name="propertysorted"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. Defaults to false.</td></tr>
</table>

Are the collections sorted. If arrays are sorted
then key comparision will be much quicker.

#### values <a name="propertyvalues"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The value property names. The value properties
decide if two beans match when their keys match.


### Examples
#### Example 1 <a name="example1"></a>

[beancmpr:array-comparer](../../../../org/oddjob/beancmpr/beans/BeanArrayComparerType.md) has an example of comparing two lists
of beans.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
