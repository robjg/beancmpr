Building Beancmpr
=============

Beancmpr builds with Maven from the project root use `maven install`.
Beancmpr requires JDK 21 or later.

Bespoke Maven targets:
- Build the reference pages: `exec:exec@reference -N -X`. Run from the
  root project.
- Build the Documentation: `exec:exec@docs` run from `beancmpr-examples`
- Build all the javadocs: `javadoc:aggregate@aggregate` run from the 
  root project

Other useful standard targets:
- Check dependencies: `versions:display-dependency-updates`
- Check plugins: `versions:display-plugin-updates`
