package com.example.eventstore.query.exception;

import java.util.function.Predicate;

public class RecordFailurePredicate implements Predicate<Throwable> {
  @Override
  public boolean test(Throwable throwable) {
    return false;
  }
}
