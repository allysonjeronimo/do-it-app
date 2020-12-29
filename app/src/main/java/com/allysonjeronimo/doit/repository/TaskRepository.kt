package com.allysonjeronimo.doit.repository

import androidx.lifecycle.LiveData
import com.allysonjeronimo.doit.data.db.entity.Task

// Usando esse padrão com interfaces, desacoplando
// a implementação do contrato, podemos mais facilmente
// realizar manutenção e testes, visto que os testes não
// irão depender dos dados vindos do banco de dados ou api
// e sim da própria memória, serão mocks (OCP - Open Closed Principle)
interface TaskRepository {
    // Continuamos usando o suspend nessa camada até o ViewModel
    // que irá utilizar essas funções usando coroutines
    suspend fun insert(description:String) : Long
    suspend fun update(id:Long, description:String, done:Boolean)
    suspend fun delete(id:Long)
    suspend fun deleteAll()
    // Utilizando LiveData (extensions do Room-LiveData) não usamos suspend
    fun findAll() : LiveData<List<Task>>
}