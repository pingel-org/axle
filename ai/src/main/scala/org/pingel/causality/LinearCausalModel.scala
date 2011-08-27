
package org.pingel.bayes

class LinearCausalModel(name: String) extends CausalModel(name) {
    

}

class LinearCausalModel(source: CausalModel) {
	// TODO this just does a shallow copy of the source model
	// It should probably to a deep copy.
	name = source.name + " (linear)"
	variable2function = source.variable2function
	newVarIndex = source.newVarIndex
	name2variable = source.name2variable
	graph = source.graph
}

