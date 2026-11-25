package com.pmdalvarez.pcambio.domain.repository

sealed interface RepositoryResult<out T> {

  data class Success<T>(
    val data: T,
  ) : RepositoryResult<T>

  data object Error : RepositoryResult<Nothing>

}