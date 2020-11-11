package cookbook.ch4_powerful_data_processing

import h1
import h2
import java.time.Instant

fun ch4() {
    h1("Ch4. Powerful Data Processing")
    h2(arrayOf(
        ::composing_and_consuming_collections_the_easy_way,
        ::filtering_datasets,
        ::automatic_null_removal,
        ::sorting_data_with_custom_comparator,
        ::building_strings_based_on_dataset_elements,
        ::dividing_data_into_subsets,
        ::data_transformation_with_map_and_flatMap,
        ::folding_and_reducing_data_sets,
        ::grouping_data,
    ))
}

data class Message(val text: String, val sender: String, val timestamp: Instant = Instant.now())
fun composing_and_consuming_collections_the_easy_way() {}
fun filtering_datasets () {}
fun automatic_null_removal () {}
fun sorting_data_with_custom_comparator () {}
fun building_strings_based_on_dataset_elements () {}
fun dividing_data_into_subsets () {}
fun data_transformation_with_map_and_flatMap () {}
fun folding_and_reducing_data_sets () {}
fun grouping_data () {}