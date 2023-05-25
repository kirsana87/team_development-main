package pro.fateeva.pillsreminder.koin

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepositoryImpl
import pro.fateeva.pillsreminder.clean.data.NotificationManager
import pro.fateeva.pillsreminder.clean.data.NotificationManagerImpl
import pro.fateeva.pillsreminder.clean.data.pillsearching.PillsApi
import pro.fateeva.pillsreminder.clean.data.pillsearching.PillsRepository
import pro.fateeva.pillsreminder.clean.data.pillsearching.RetrofitPillsApiClient
import pro.fateeva.pillsreminder.clean.data.pillsearching.SearchPillsRepository
import pro.fateeva.pillsreminder.clean.data.room.LocalMedicationDatabase
import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.clean.domain.NotificationHandlingInteractor
import pro.fateeva.pillsreminder.ui.mainactivity.MainViewModel
import pro.fateeva.pillsreminder.ui.screens.calendar.ScheduleCalendarViewModel
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsViewModel
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillViewModel
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListViewModel
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_API_URL = "https://app.rlsnet.ru/api/"

object Di {
    val mainModule = module {
        single { MedicationInteractor(get(), get()) }
        single { NotificationHandlingInteractor(repository = get()) }
        single<NotificationManager> { NotificationManagerImpl(get()) }
        single<MedicationReminderRepository> {
            MedicationReminderRepositoryImpl(
                reminderDao = get(),
                intakeDao = get(),
                mapper = get())
        }
        single<SearchPillsRepository> { PillsRepository(retrofit = get()) }
        factory { MedicationEntityMapper() }

        viewModel { OncePerDaySettingsViewModel(get(), get()) }
        viewModel { PillsListViewModel(get()) }
        viewModel { TwicePerDaySettingsViewModel(get(), get()) }
        viewModel { ScheduleCalendarViewModel(repository = get()) }
        viewModel { SearchPillViewModel(searchPillRepository = get()) }
        viewModel { MainViewModel(interactor = get()) }
    }

    val roomModule = module {
        single { LocalMedicationDatabase.getUserDatabase(androidContext()).medicationReminderDao }
        single { LocalMedicationDatabase.getUserDatabase(androidContext()).medicationIntakeDao }
    }

    val retrofitModule = module {
        single {
            Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient()
                    .create()))
                .client(RetrofitPillsApiClient().createClient())
                .build().create(PillsApi::class.java)
        }
    }
}