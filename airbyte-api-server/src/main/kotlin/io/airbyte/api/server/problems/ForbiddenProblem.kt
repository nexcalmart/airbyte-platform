/*
 * Copyright (c) 2020-2024 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.api.server.problems

import io.airbyte.api.server.constants.API_DOC_URL
import io.micronaut.http.HttpStatus
import io.micronaut.problem.HttpStatusType
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import java.io.Serial
import java.net.URI

/**
 * For throwing forbidden errors.
 */
class ForbiddenProblem(message: String?) : AbstractThrowableProblem(
  TYPE,
  TITLE,
  HttpStatusType(HttpStatus.FORBIDDEN),
  message,
) {
  companion object {
    @Serial
    private val serialVersionUID = 1L
    private val TYPE = URI.create("$API_DOC_URL/reference/errors#forbidden")
    private const val TITLE = "forbidden"
  }

  override fun getCause(): Exceptional? {
    return null
  }
}
