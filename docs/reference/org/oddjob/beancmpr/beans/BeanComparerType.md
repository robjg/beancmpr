[HOME](../../../../README.md)
# beancmpr:bean-comparer

Provides a definition for how to compare two Beans
by the properties of the beans, or how to compare two Objects just by
their values.



### Property Summary

| Property | Description |
| -------- | ----------- |
| [comparersByName](#propertycomparersbyname) | Comparers for comparing the properties of the beans defined by the name of the property. | 
| [comparersByType](#propertycomparersbytype) | Comparers for comparing the properties of the beans. | 
| [others](#propertyothers) | Names of properties that to use for the comparison. | 
| [values](#propertyvalues) | Names of properties to use for the comparison. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Comparing two number. |
| [Example 2](#example2) | Comparing two bean by their properties. |
| [Example 3](#example3) | Specifying a Bean Comparer for the Property of a Bean. |


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

#### others <a name="propertyothers"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. If no property names are given then the
 objects themselves are used for the comparison.</td></tr>
</table>

Names of properties that to use for the comparison.

#### values <a name="propertyvalues"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No. If no property names are given then the
 objects themselves are used for the comparison.</td></tr>
</table>

Names of properties to use for the comparison.


### Examples
#### Example 1 <a name="example1"></a>

Comparing two number. The comparer declares a numeric comparer for
doubles with a tolerance of 0.1 so the two numbers are deemed equal.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
    <job>
        <sequential>
            <jobs>
                <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
                    <inX>
                        <convert>
                            <to>
                                <class name="double"/>
                            </to>
                            <value>
                                <value value="5.2"/>
                            </value>
                        </convert>
                    </inX>
                    <inY>
                        <convert>
                            <to>
                                <class name="double"/>
                            </to>
                            <value>
                                <value value="5.3"/>
                            </value>
                        </convert>
                    </inY>
                    <comparer>
                        <beancmpr:bean-comparer>
                            <comparersByType>
                                <beancmpr:comparers-by-type>
                                    <specialisations>
                                        <beancmpr:numeric-comparer deltaTolerance="0.1" key="double"/>
                                    </specialisations>
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


The output is:

```
matchResultType  xValue  yValue  valueComparison
---------------  ------  ------  ---------------
EQUAL            5.2     5.3
```


#### Example 2 <a name="example2"></a>

Comparing two bean by their properties. A case insensitve text comparer is
specified for type but not for colour so the beans do not match on colour.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
  <job>
    <sequential>
      <jobs>
        <beancmpr:compare id="compare" xmlns:beancmpr="oddjob:beancmpr">
          <inX>
            <bean class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
              colour="green" type="apple" />
          </inX>
          <inY>
            <bean class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
              colour="GREEN" type="APPLE" />
          </inY>
          <results>
            <beancmpr:bean-results>
              <out>
                <list />
              </out>
            </beancmpr:bean-results>
          </results>
          <comparer>
            <beancmpr:bean-comparer values="type, colour">
              <comparersByName>
                <beancmpr:comparers-by-name>
                  <comparers>
                    <beancmpr:text-comparer key="type"
                      ignoreCase="true" />
                  </comparers>
                </beancmpr:comparers-by-name>
              </comparersByName>
            </beancmpr:bean-comparer>
          </comparer>
        </beancmpr:compare>
        <bean-report>
          <beans>
            <value value="${compare.results.out}" />
          </beans>
        </bean-report>
      </jobs>
    </sequential>
  </job>
</oddjob>
```


The output is:

```
matchResultType  xType  yType  typeComparison  xColour  yColour  colourComparison
---------------  -----  -----  --------------  -------  -------  ----------------
NOT_EQUAL        apple  APPLE                  green    GREEN    green<>GREEN
```


#### Example 3 <a name="example3"></a>

Specifying a Bean Comparer for the Property of a Bean. Here we specify the
comparer to be used for the fruit property of a Snack class.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<oddjob>
  <job>
    <sequential>
      <jobs>
        <variables id="vars">
          <listA>
            <list>
              <values>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="1">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      type="banana" />
                  </fruit>
                </bean>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="2">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      colour="green" type="APPLE" />
                  </fruit>
                </bean>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="3">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      colour="pear" type="red" />
                  </fruit>
                </bean>
              </values>
            </list>
          </listA>
          <listB>
            <list>
              <values>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="1">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      type="banana" />
                  </fruit>
                </bean>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="2">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      colour="green" type="apple" />
                  </fruit>
                </bean>
                <bean
                  class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Snack"
                  id="3">
                  <fruit>
                    <bean
                      class="org.oddjob.beancmpr.beans.BeanComparerTypeExamplesTest$Fruit"
                      colour="pear" type="green" />
                  </fruit>
                </bean>
              </values>
            </list>
          </listB>
        </variables>
        <beancmpr:compare id="compare"
          xmlns:beancmpr="oddjob:beancmpr">
          <inX>
            <value value="${vars.listA}" />
          </inX>
          <inY>
            <value value="${vars.listB}" />
          </inY>
          <results>
            <beancmpr:bean-results>
              <out>
                <list />
              </out>
            </beancmpr:bean-results>
          </results>
          <comparer>
            <beancmpr:collection-comparer keys="id" values="fruit">
              <comparersByName>
                <beancmpr:comparers-by-name>
                  <comparers>
                    <beancmpr:bean-comparer key="fruit"
                      values="type, colour" />
                  </comparers>
                </beancmpr:comparers-by-name>
              </comparersByName>
              <comparersByType>
                <beancmpr:comparers-by-type>
                  <comparers>
                    <beancmpr:text-comparer
                      ignoreCase="true" />
                  </comparers>
                </beancmpr:comparers-by-type>
              </comparersByType>
            </beancmpr:collection-comparer>
          </comparer>
        </beancmpr:compare>
        <bean-report>
          <beans>
            <value value="${compare.results.out}" />
          </beans>
        </bean-report>
      </jobs>
    </sequential>
  </job>
</oddjob>
```


The output is:

```
matchResultType  id  xFruit                           yFruit                           fruitComparison
---------------  --  -------------------------------  -------------------------------  ----------------
EQUAL            1   Fruit: type=banana, colour=null  Fruit: type=banana, colour=null  2/2 values equal
EQUAL            2   Fruit: type=APPLE, colour=green  Fruit: type=apple, colour=green  2/2 values equal
NOT_EQUAL        3   Fruit: type=red, colour=pear     Fruit: type=green, colour=pear   1/2 values equal
```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
