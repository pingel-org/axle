package axle.ml

/**
 * 
 * 
 * http://en.wikipedia.org/wiki/Hidden_Markov_model
 * 
 */

case class MarkovModelState(observable: Boolean)

// Graph of MarkovModelState
// where un-observables can be fully connected
// and there are arrows from un-observables to observables

class HiddenMarkovModel()
{

}