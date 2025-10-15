[HOME](../../../../README.md)
# beancmpr:map-comparer

Provides a Comparer that can compare two maps. The maps
will be compared first by their keys to decide if entries are missing, and
then by their values to decide if entries match. Comparison of keys and
values can be either via the actual key and value or by bean properties of
the keys and values.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparersByName](#propertycomparersbyname) | Sub Comparers to use for comparison of the named properites of keys or values. | 
| [comparersByType](#propertycomparersbytype) | Sub Comparers to use for comparison based on the types of properties or values. | 
| [keys](#propertykeys) | The property names for the key. | 
| [others](#propertyothers) | The property names for other properties to be reported on as part of the results but not used for comparison. | 
| [sorted](#propertysorted) | Is the map sorted or not. | 
| [values](#propertyvalues) | The property names for the value. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Comparing two maps by the values of their keys and values. |
| [Example 2](#example2) | Comparing two Beans by a property that is a Map. |
| [Example 3](#example3) | Comparing two Maps of Beans by the properties of their Keys and Values. |


### Property Detail
#### comparersByName <a name="propertycomparersbyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Sub Comparers to use for comparison of the named
properites of keys or values. Note that if the key and value have a
property of the same name then this applies to both.

#### comparersByType <a name="propertycomparersbytype"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Sub Comparers to use for comparison based on
the types of properties or values.

#### keys <a name="propertykeys"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. When missing the value of the key is used for comparison.</td></tr>
</table>

The property names for the key. The key
properties decided if an entry is missing or not.

#### others <a name="propertyothers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The property names for other properties to
be reported on as part of the results but not used for comparison.

#### sorted <a name="propertysorted"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

Is the map sorted or not.

#### values <a name="propertyvalues"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. When missing the value itself is used for comparison.</td></tr>
</table>

The property names for the value. When two keys
match their values are compared using these properties.


### Examples
#### Example 1 <a name="example1"></a>

Comparing two maps by the values of their keys and values. The comparer declares
a text comparer which is case insensitive and this is used for comparison
of the keys.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <variables id="test-data">
                    <mapX>
                        <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap1">
                            <fruit>
                                <value key="apple" value="42"/>
                                <value key="pear" value="15"/>
                                <value key="avacardo" value="7"/>
                            </fruit>
                        </bean>
                    </mapX>
                    <mapY>
                        <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap1">
                            <fruit>
                                <value key="APPLE" value="17"/>
                                <value key="PEAR" value="15"/>
                                <value key="KIWI" value="5"/>
                            </fruit>
                        </bean>
                    </mapY>
                </variables>
                <beancmpr:compare id="compare-maps" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <value value="${test-data.mapX}"/>
                    </inX>
                    <inY>
                        <value value="${test-data.mapY}"/>
                    </inY>
                    <comparer>
                        <beancmpr:map-comparer>
                            <comparersByType>
                                <beancmpr:comparers-by-type>
                                    <comparers>
                                        <beancmpr:text-comparer ignoreCase="true"/>
                                    </comparers>
                                </beancmpr:comparers-by-type>
                            </comparersByType>
                        </beancmpr:map-comparer>
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
                        <value value="${compare-maps.results.out}"/>
                    </beans>
                </bean-report>
            </jobs>
        </sequential>
    </job>
</oddjob>
```


The output is:

```
matchResultType  key       xValue  yValue  valueComparison
---------------  --------  ------  ------  ---------------
NOT_EQUAL        apple     42      17      42<>17
y_MISSING        avacardo  7               
x_MISSING        KIWI              5       
EQUAL            pear      15      15
```


#### Example 2 <a name="example2"></a>

Comparing two Beans by a property that is a Map.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <variables id="test-data">
                    <beanX>
                        <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$MapBean1">
                            <fruitMap>
                                <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap1">
                                    <fruit>
                                        <value key="apple" value="42"/>
                                        <value key="pear" value="15"/>
                                    </fruit>
                                </bean>
                            </fruitMap>
                        </bean>
                    </beanX>
                    <beanY>
                        <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$MapBean1">
                            <fruitMap>
                                <bean class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap1">
                                    <fruit>
                                        <value key="APPLE" value="42"/>
                                        <value key="PEAR" value="15"/>
                                    </fruit>
                                </bean>
                            </fruitMap>
                        </bean>
                    </beanY>
                </variables>
                <beancmpr:compare id="compare-maps" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <value value="${test-data.beanX}"/>
                    </inX>
                    <inY>
                        <value value="${test-data.beanY}"/>
                    </inY>
                    <comparer>
                        <beancmpr:bean-comparer values="fruitMap">
                            <comparersByName>
                                <beancmpr:comparers-by-name>
                                    <comparers>
                                        <beancmpr:map-comparer key="fruitMap"/>
                                    </comparers>
                                </beancmpr:comparers-by-name>
                            </comparersByName>
                            <comparersByType>
                                <beancmpr:comparers-by-type>
                                    <comparers>
                                        <beancmpr:text-comparer ignoreCase="true"/>
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
                        <value value="${compare-maps.results.out}"/>
                    </beans>
                </bean-report>
            </jobs>
        </sequential>
    </job>
</oddjob>
```


The output is:

```
matchResultType  xFruitMap            yFruitMap            fruitMapComparison
---------------  -------------------  -------------------  ------------------
EQUAL            {apple=42, pear=15}  {APPLE=42, PEAR=15}
```


#### Example 3 <a name="example3"></a>

Comparing two Maps of Beans by the properties of their Keys and Values.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
  <job>
    <sequential>
      <jobs>
        <variables id="test-data">
          <mapX>
            <bean
              class="org.oddjob.beancmpr.beans.MapComparerTypeTest$MapBean2">
              <fruitMap>
                <bean
                  class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap2">
                  <entries>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="green" fruit="apple" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="13.9" quantity="42" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="red" fruit="apple" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="15.9" quantity="30" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="yellow" fruit="banana" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="16.0" quantity="5" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="pink" fruit="gratefruit" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="42.0" quantity="2" />
                      </value>
                    </bean>
                  </entries>
                </bean>
              </fruitMap>
            </bean>
          </mapX>
          <mapY>
            <bean
              class="org.oddjob.beancmpr.beans.MapComparerTypeTest$MapBean2">
              <fruitMap>
                <bean
                  class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMap2">
                  <entries>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="green" fruit="pear" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="12.5" quantity="24" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="red" fruit="apple" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="16.1" quantity="30" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="yellow" fruit="banana" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="16.0" quantity="6" />
                      </value>
                    </bean>
                    <bean
                      class="org.oddjob.beancmpr.beans.MapComparerTypeTest$FruitMapEntry">
                      <key>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$KeyBean"
                          colour="pink" fruit="gratefruit" />
                      </key>
                      <value>
                        <bean
                          class="org.oddjob.beancmpr.beans.MapComparerTypeTest$ValueBean"
                          price="42.0" quantity="2" />
                      </value>
                    </bean>
                  </entries>
                </bean>
              </fruitMap>
            </bean>
          </mapY>
        </variables>
        <beancmpr:compare id="compare-maps"
          xmlns:beancmpr="oddjob:beancmpr">
          <inX>
            <value value="${test-data.mapX}" />
          </inX>
          <inY>
            <value value="${test-data.mapY}" />
          </inY>
          <results>
            <beancmpr:bean-results>
              <out>
                <list />
              </out>
            </beancmpr:bean-results>
          </results>
          <comparer>
            <beancmpr:map-comparer keys="colour, fruit"
              values="quantity, price">
              <comparersByType>
                <beancmpr:comparers-by-type>
                  <specialisations>
                    <beancmpr:numeric-comparer
                      deltaTolerance="1" key="double" />
                  </specialisations>
                </beancmpr:comparers-by-type>
              </comparersByType>
            </beancmpr:map-comparer>
          </comparer>
        </beancmpr:compare>
        <bean-report>
          <beans>
            <value value="${compare-maps.results.out}" />
          </beans>
        </bean-report>
      </jobs>
    </sequential>
  </job>
</oddjob>
```


The output is:

```
matchResultType  colour  fruit       xQuantity  yQuantity  quantityComparison  xPrice  yPrice  priceComparison
---------------  ------  ----------  ---------  ---------  ------------------  ------  ------  ---------------
y_MISSING        green   apple       42                                        13.9            
x_MISSING        green   pear                   24                                     12.5    
EQUAL            pink    gratefruit  2          2                              42.0    42.0    
EQUAL            red     apple       30         30                             15.9    16.1    
NOT_EQUAL        yellow  banana      5          6          5<>6                16.0    16.0
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
