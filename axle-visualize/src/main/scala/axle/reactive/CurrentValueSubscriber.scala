package axle.reactive

import scala.concurrent.Future
import monix.reactive.observers.Subscriber
import monix.execution.Ack
import monix.execution.Scheduler
import monix.execution.Ack.Continue

/**
 * CurrentValueSubscriber
 *
 * TODO: Investigate this approach suggested by the creator of Monix:
 *
 * You can cache the last emitted value by doing a
 * observable.multicast(Pipe.behavior(initial)),
 * which has a shortcut, so you can do observable.behavior(initial).
 *
 * As the name suggests, this turns your source into a multicast / hot data-source,
 * meaning that it gets shared between multiple subscribers,
 * hence the returned type is ConnectableObservable,
 * which requires a .connect() in order to actually start the source.
 *
 */

class CurrentValueSubscriber[T](implicit _sched: Scheduler)
    extends Subscriber[T] {

  private[this] var _cv: Option[T] = None

  def currentValue: Option[T] = _cv

  def onNext(elem: T): Future[Ack] = {
    _cv = Option(elem)
    Continue
  }

  def onComplete(): Unit = {}

  def onError(ex: Throwable): Unit = {}

  def scheduler: Scheduler = _sched
}