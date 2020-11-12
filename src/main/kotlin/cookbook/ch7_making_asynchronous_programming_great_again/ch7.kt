package cookbook.ch7_making_asynchronous_programming_great_again

import h1
import h2

fun ch7() {
    h1("Ch7. Making Asynchronous Programming Great Again")
    h2(arrayOf(
        ::executing_tasks_in_the_background_using_threads,
        ::background_threads_synchronization,
        ::using_coroutines_for_asynchronous_concurrent_execution_of_tasks,
        ::using_coroutines_for_asynchronous_concurrent_execution_with_results_handling,
        ::applying_coroutines_for_asynchronous_data_processing,
        ::easy_coroutine_cancelation,
        ::building_a_REST_API_client_with_Retrofit_and_coroutines_adapter,
        ::wrapping_third_party_callback_style_APIs_with_coroutines
    ))
}

fun executing_tasks_in_the_background_using_threads() {}
fun background_threads_synchronization() {}
fun using_coroutines_for_asynchronous_concurrent_execution_of_tasks() {}
fun using_coroutines_for_asynchronous_concurrent_execution_with_results_handling() {}
fun applying_coroutines_for_asynchronous_data_processing() {}
fun easy_coroutine_cancelation() {}
fun building_a_REST_API_client_with_Retrofit_and_coroutines_adapter() {}
fun wrapping_third_party_callback_style_APIs_with_coroutines() {}