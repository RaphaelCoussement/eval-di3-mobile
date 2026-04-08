package di

import data.local.LocationLocalDataSource
import data.remote.LocationRemoteDataSource
import data.remote.LocationRemoteDataSourceImpl
import data.repository.LocationRepositoryImpl
import domain.repository.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import presentation.locationdetail.LocationDetailViewModel
import presentation.locationlist.LocationListViewModel

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // permet de ne pas faire crash l'app si la réponse change
                })
            }
        }
    }

    // DataSources
    single<LocationRemoteDataSource> { LocationRemoteDataSourceImpl(get()) }
    single { LocationLocalDataSource() }

    // Repository
    single<LocationRepository> { LocationRepositoryImpl(get(), get()) }

    // ViewModels
    factory { LocationListViewModel(get()) }
    factory { params -> LocationDetailViewModel(locationId = params.get(), repository = get()) }
}