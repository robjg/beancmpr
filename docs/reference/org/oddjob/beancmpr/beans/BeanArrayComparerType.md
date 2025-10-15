[HOME](../../../../README.md)
# beancmpr:array-comparer

Compares an Array of Java Beans. If no Java Bean
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
| [sorted](#propertysorted) | Are the arrays sorted. | 
| [values](#propertyvalues) | The value property names. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Comparing two arrays of beans. |
| [Example 2](#example2) | Comparing two arrays of ints. |
| [Example 3](#example3) | Specifying how to compare an array that is the property of two beans being compared. |


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

Are the arrays sorted. If arrays are sorted
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

Comparing two arrays of beans. Properties of the beans are used to make
the comparison.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
  <job>
    <sequential name="Bean Array Example">
      <jobs>
        <bean class="org.oddjob.beancmpr.SharedTestData" id="test-data" />
        <beancmpr:compare id="compare-bean-arrays"
          xmlns:beancmpr="oddjob:beancmpr">
          <inX>
            <value value="${test-data.arrayFruitX}" />
          </inX>
          <inY>
            <value value="${test-data.arrayFruitY}" />
          </inY>
          <comparer>
            <beancmpr:array-comparer keys="id"
              others="colour" values="type, quantity, price">
              <comparersByType>
                <beancmpr:comparers-by-type>
                  <specialisations>
                    <beancmpr:numeric-comparer
                      deltaTolerance="0.05" key="double" />
                  </specialisations>
                </beancmpr:comparers-by-type>
              </comparersByType>
            </beancmpr:array-comparer>
          </comparer>
          <results>
            <beancmpr:bean-results>
              <out>
                <list />
              </out>
            </beancmpr:bean-results>
          </results>
        </beancmpr:compare>
        <bean-report>
          <beans>
            <value value="${compare-bean-arrays.results.out}" />
          </beans>
        </bean-report>
      </jobs>
    </sequential>
  </job>
</oddjob>
```


The output is:

```
matchResultType  id  xType   yType   typeComparison  xQuantity  yQuantity  quantityComparison  xPrice  yPrice  priceComparison  xColour  yColour
---------------  --  ------  ------  --------------  ---------  ---------  ------------------  ------  ------  ---------------  -------  -------
EQUAL            1   Apple   Apple                   4          4                              54.56   54.57                    green    red
NOT_EQUAL        2   Banana  Banana                  3          4          3<>4                23.24   23.25                    yellow   yellow
x_MISSING        3           Orange                             2                                      80.05                             orange
y_MISSING        5   Orange                          2                                         70.95                            orange
```


#### Example 2 <a name="example2"></a>

Comparing two arrays of ints.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
  <job>
    <sequential name="Int Array Example">
      <jobs>
        <beancmpr:compare id="compare-int-arrays"
          xmlns:beancmpr="oddjob:beancmpr">
          <inX>
            <convert>
              <to>
                <class name="[I" />
              </to>
              <value>
                <tokenizer text="1, 2, 4, 5" />
              </value>
            </convert>
          </inX>
          <inY>
            <convert>
              <to>
                <class name="[I" />
              </to>
              <value>
                <tokenizer text="5, 4, 3, 1, 7" />
              </value>
            </convert>
          </inY>
          <results>
            <beancmpr:bean-results>
              <out>
                <list />
              </out>
            </beancmpr:bean-results>
          </results>
        </beancmpr:compare>
        <bean-report>
          <beans>
            <value value="${compare-int-arrays.results.out}" />
          </beans>
        </bean-report>
      </jobs>
    </sequential>
  </job>
</oddjob>
```


The output is:

```
matchResultType  value
---------------  -----
EQUAL            1
y_MISSING        2
x_MISSING        3
EQUAL            4
EQUAL            5
x_MISSING        7
```


#### Example 3 <a name="example3"></a>

Specifying how to compare an array that is the property of two beans
being compared.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <bean class="org.oddjob.beancmpr.beans.BeanArrayComparerTypeTest$ArrayPropertyTestData" id="test-data"/>
                <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <value value="${test-data.x}"/>
                    </inX>
                    <inY>
                        <value value="${test-data.y}"/>
                    </inY>
                    <comparer>
                        <beancmpr:bean-comparer values="numbers">
                            <comparersByName>
                                <beancmpr:comparers-by-name>
                                    <comparers>
                                        <beancmpr:array-comparer key="numbers">
                                            <comparersByType>
                                                <beancmpr:comparers-by-type>
                                                    <specialisations>
                                                        <beancmpr:numeric-comparer deltaTolerance="1.0" key="double"/>
                                                    </specialisations>
                                                </beancmpr:comparers-by-type>
                                            </comparersByType>
                                        </beancmpr:array-comparer>
                                    </comparers>
                                </beancmpr:comparers-by-name>
                            </comparersByName>
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


The output is:

```
matchResultType  xNumbers     yNumbers     numbersComparison
---------------  -----------  -----------  -----------------
EQUAL            [D@4ba6b85d  [D@2e920878
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
