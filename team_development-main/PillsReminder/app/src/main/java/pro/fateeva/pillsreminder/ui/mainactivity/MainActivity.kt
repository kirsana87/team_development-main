package pro.fateeva.pillsreminder.ui.mainactivity

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.ui.navigation.NavigationFragment
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener
import pro.fateeva.pillsreminder.ui.screens.calendar.ScheduleCalendarFragment
import pro.fateeva.pillsreminder.ui.screens.frequency.FrequencyFragment
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsFragment
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillFragment
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListFragment
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsFragment

private const val NAVIGATION_BACKSTACK_NAME = "NAVIGATION_BACKSTACK"

class MainActivity : AppCompatActivity(), NotificationHandler, AppNavigation {

    private val viewModel by viewModel<MainViewModel>()

    override val alarmManager: AlarmManager by lazy {
        getSystemService(ALARM_SERVICE) as AlarmManager
    }

    override val actionListener: NotificationActionListener by lazy {
        MedicationActionListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, NavigationFragment())
                .commit()
        }
    }

    override fun onGetDrugAction(pillID: Int, plannedMedicationTime: Long, message: String) {
        viewModel.setActualMedicationTime(pillID, plannedMedicationTime, System.currentTimeMillis())
        showToast(message)
    }

    override fun onCancelDrugAction(message: String) {
        showToast(message)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        actionListener.onNotificationAction(this, applicationContext, intent)
    }

    override fun navigateToPillListScreen(isNavigationBottomAction: Boolean) {
        navigateToDestination(PillsListFragment(), isNavigationBottomAction)
    }

    override fun navigateToPillSearchingScreen() {
        navigateToDestination(SearchPillFragment())
    }

    override fun navigateToEventFrequencyScreen(drugDomain: DrugDomain) {
        navigateToDestination(FrequencyFragment.newInstance(drugDomain))
    }

    override fun navigateToOncePerDayScreen(drugDomain: DrugDomain, daysCount: Int) {
        navigateToDestination(OncePerDaySettingsFragment.newInstance(drugDomain, daysCount))
    }

    override fun navigateToOncePerDayScreen(id: Int) {
        navigateToDestination(OncePerDaySettingsFragment.newInstance(id))
    }

    override fun navigateToTwicePerDayScreen(drugDomain: DrugDomain, daysCount: Int) {
        navigateToDestination(TwicePerDaySettingsFragment.newInstance(drugDomain, daysCount))
    }

    override fun navigateToTwicePerDayScreen(id: Int) {
        navigateToDestination(TwicePerDaySettingsFragment.newInstance(id))
    }

    override fun navigateToScheduleCalendarScreen(isNavigationBottomAction: Boolean) {
        navigateToDestination(ScheduleCalendarFragment(), isNavigationBottomAction)
    }

    override fun navigateToDestination(destination: Fragment, isNavigationBottomAction: Boolean) {
        if (isNavigationBottomAction) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.navigation_container, destination)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.navigation_container, destination)
                .addToBackStack(NAVIGATION_BACKSTACK_NAME)
                .commit()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}