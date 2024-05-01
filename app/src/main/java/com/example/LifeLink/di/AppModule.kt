package ro.alexmamo.roomjetpackcompose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.LifeLink.Service.DefaultLocationTracker
import com.example.LifeLink.Service.LocationTracker
import com.example.LifeLink.Service.NotificationService
import com.example.LifeLink.data.dao.IDao
import com.example.LifeLink.data.interfaces.IContactRepo
import com.example.LifeLink.data.interfaces.IMedicalRepo
import com.example.LifeLink.data.interfaces.IUserRepo
import com.example.LifeLink.data.repository.ContactRepository
import com.example.LifeLink.data.network.DbContext
import com.example.LifeLink.data.repository.MedicalRepository
import com.example.LifeLink.data.repository.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideLifeLinkDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        DbContext::class.java,
        "LifeLink.db"
    ).allowMainThreadQueries().build()

    @Provides
    fun provideUserDao(
        ctx : DbContext
    ) = ctx.dao

    @Provides
    fun provideContactRepository(
        dao : IDao
    ): IContactRepo = ContactRepository(
        dao
    )

    @Provides
    fun provideUserRepository(
        dao : IDao
    ): IUserRepo = UserRepository(
        dao
    )
    @Provides
    fun provideMedicalRepository(
        dao : IDao
    ): IMedicalRepo = MedicalRepository(
        dao
    )

    //location
    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )

    @Provides
    fun providesNotification(
        context:Context
    ): NotificationService = NotificationService(context
    )
}