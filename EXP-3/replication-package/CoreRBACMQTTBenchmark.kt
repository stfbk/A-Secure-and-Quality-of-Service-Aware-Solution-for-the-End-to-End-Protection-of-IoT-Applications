package benchmarks

import benchmarks.TestUtilities.Companion.RandomNamesState
import benchmarks.TestUtilities.Companion.addAndInitUser
import benchmarks.TestUtilities.Companion.assertSuccessOrThrow
import benchmarks.TestUtilities.Companion.coreRBACMQTTNoAC
import eu.fbk.st.cryptoac.generateRandomString
import eu.fbk.st.cryptoac.inputStream
import eu.fbk.st.cryptoac.model.tuple.PermissionType
import eu.fbk.st.cryptoac.model.unit.EnforcementType.COMBINED
import org.openjdk.jmh.annotations.*
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkInitAdmin {

    @TearDown(Level.Iteration)
    fun tearDownTrial() {
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun initAdmin() {
        coreRBACMQTTNoAC.configureServices()
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAddUser {

    private var usernames = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun addUser() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.addUser(usernames.getNextName()).code)
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAddRole {

    private var roleNames  = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun addRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleNames.getNextName()))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAddResource {

    private var resourceNames = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun addResource() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.addResource(
            resourceNames.getNextName().replace("+", ""),
            generateRandomString(1024).inputStream(),
            COMBINED
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkDeleteResource {

    private var resourceNamesAlreadyPresent = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            assertSuccessOrThrow(
                coreRBACMQTTNoAC.addResource(
                    resourceNamesAlreadyPresent.getNextName().replace("+", ""),
                    "none".inputStream(),
                    COMBINED
                )
            )
        }
        resourceNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun deleteResource() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.deleteResource(
            resourceNamesAlreadyPresent.getNextName().replace("+", ""),
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAssignUserToRole {

    var usernamesAlreadyPresent = RandomNamesState()
    var roleNamesAlreadyPresent  = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, usernamesAlreadyPresent.getNextName()))
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleNamesAlreadyPresent.getNextName()))
        }
        usernamesAlreadyPresent.reset()
        roleNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun assignUserToRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            usernamesAlreadyPresent.getNextName(),
            roleNamesAlreadyPresent.getNextName(),
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAssignNewPermissionToRole {

    var roleNamesAlreadyPresent  = RandomNamesState()
    var resourceNamesAlreadyPresent = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleNamesAlreadyPresent.getNextName()))
            assertSuccessOrThrow(
                coreRBACMQTTNoAC.addResource(
                    resourceNamesAlreadyPresent.getNextName().replace("+", ""),
                    "none".inputStream(),
                    COMBINED
                )
            )
        }
        roleNamesAlreadyPresent.reset()
        resourceNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun assignNewPermissionToRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
            roleNamesAlreadyPresent.getNextName(),
            resourceNamesAlreadyPresent.getNextName().replace("+", ""),
            PermissionType.READ
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkAssignExistingPermissionToRole {

    private var roleNamesAlreadyPresent  = RandomNamesState()
    private var resourceNamesAlreadyPresent = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            val currentRoleName = roleNamesAlreadyPresent.getNextName()
            val currentResourceName = resourceNamesAlreadyPresent.getNextName().replace("+", "")
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
            assertSuccessOrThrow(
                coreRBACMQTTNoAC.addResource(
                    currentResourceName.replace("+", ""),
                    "none".inputStream(),
                    COMBINED
                )
            )
            assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
                currentRoleName,
                currentResourceName,
                PermissionType.READ
            ))
        }
        roleNamesAlreadyPresent.reset()
        resourceNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun assignExistingPermissionToRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
            roleNamesAlreadyPresent.getNextName(),
            resourceNamesAlreadyPresent.getNextName().replace("+", ""),
            PermissionType.READWRITE
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeUserFromRole {

    private var usernamesAlreadyPresent = RandomNamesState()
    private var roleNamesAlreadyPresent  = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            val currentUserName = usernamesAlreadyPresent.getNextName()
            val currentRoleName = roleNamesAlreadyPresent.getNextName()
            assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, currentUserName))
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
            assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
                currentUserName,
                currentRoleName,
            ))
        }
        usernamesAlreadyPresent.reset()
        roleNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun revokeUserFromRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.revokeUserFromRole(
            usernamesAlreadyPresent.getNextName(),
            roleNamesAlreadyPresent.getNextName(),
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeOnePermissionFromRole {

    private var roleNamesAlreadyPresent  = RandomNamesState()
    private var resourceNamesAlreadyPresent = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            val currentRoleName = roleNamesAlreadyPresent.getNextName()
            val currentResourceName = resourceNamesAlreadyPresent.getNextName().replace("+", "")
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
            assertSuccessOrThrow(
                coreRBACMQTTNoAC.addResource(
                    currentResourceName.replace("+", ""),
                    "none".inputStream(),
                    COMBINED
                )
            )
            assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
                currentRoleName,
                currentResourceName,
                PermissionType.READWRITE
            ))
        }
        roleNamesAlreadyPresent.reset()
        resourceNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun revokeOnePermissionFromRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.revokePermissionFromRole(
            roleNamesAlreadyPresent.getNextName(),
            resourceNamesAlreadyPresent.getNextName().replace("+", ""),
            PermissionType.WRITE
        ))
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1000, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeAllPermissionsFromRole {

    private var roleNamesAlreadyPresent  = RandomNamesState()
    private var resourceNamesAlreadyPresent = RandomNamesState()

    @Setup(Level.Trial)
    fun setUpTrial() {
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        for (i in 0 until (100 + 1000)) {
            val currentRoleName = roleNamesAlreadyPresent.getNextName()
            val currentResourceName = resourceNamesAlreadyPresent.getNextName().replace("+", "")
            assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
            assertSuccessOrThrow(
                coreRBACMQTTNoAC.addResource(
                    currentResourceName.replace("+", ""),
                    "none".inputStream(),
                    COMBINED
                )
            )
            assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
                currentRoleName,
                currentResourceName,
                PermissionType.READWRITE
            ))
        }
        roleNamesAlreadyPresent.reset()
        resourceNamesAlreadyPresent.reset()
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Benchmark
    fun revokeAllPermissionsFromRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.revokePermissionFromRole(
            roleNamesAlreadyPresent.getNextName(),
            resourceNamesAlreadyPresent.getNextName().replace("+", ""),
            PermissionType.READWRITE
        ))
    }
}



// ===== ===== ===== BELOW, BENCHMARK FOR REVOKEP ACTION ===== ===== =====
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 101, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeAllPermissionsFromRoleIterateThroughParameters {

    private var roleNamesAlreadyPresent  = RandomNamesState()
    private var roleName = "roleNameUnderTest"
    private var resourceName = "resourceNameUnderTest"

    private var step = 5
    private var iteration = 0

    @Setup(Level.Trial)
    fun setUpTrial() {
        println("setUpTrial")
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleName))
        assertSuccessOrThrow(
            coreRBACMQTTNoAC.addResource(
                resourceName,
                "none".inputStream(),
                COMBINED
            )
        )
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
            roleName,
            resourceName,
            PermissionType.READWRITE
        ))
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        println("tearDownTrial")
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Setup(Level.Iteration)
    fun setUpIteration() {
        println("setUpIteration: started iteration number $iteration")
        if (iteration != 0) {
            for (i in 0 until step) {
                val currentRoleName = roleNamesAlreadyPresent.getNextName()
                assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
                assertSuccessOrThrow(
                    coreRBACMQTTNoAC.assignPermissionToRole(
                        currentRoleName,
                        resourceName,
                        PermissionType.READWRITE
                    )
                )
            }
        }
    }

    @TearDown(Level.Iteration)
    fun tearDownIteration() {
        println("tearDownTrial: concluded iteration number $iteration")
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
            roleName,
            resourceName,
            PermissionType.READWRITE
        ))
        iteration++
    }

    @Benchmark
    fun revokeAllPermissionsFromRole() {
        assertSuccessOrThrow(coreRBACMQTTNoAC.revokePermissionFromRole(
            roleName,
            resourceName,
            PermissionType.READWRITE
        ))
    }
}



// ===== ===== ===== BELOW, BENCHMARK FOR REVOKEU ACTION ===== ===== =====
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 101, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeUserFromRoleIterateThroughParameter1 {

    private var usernamesAlreadyPresent  = RandomNamesState()
    private var username = "usernameUnderTest"
    private var roleName = "roleNameUnderTest"

    private var step = 5
    private var iteration = 0

    @Setup(Level.Trial)
    fun setUpTrial() {
        println("setUpTrial")
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, username))
        assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleName))
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        println("tearDownTrial")
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Setup(Level.Iteration)
    fun setUpIteration() {
        println("setUpIteration: started iteration number $iteration")
        if (iteration != 0) {
            for (i in 0 until step) {
                val currentUsername = usernamesAlreadyPresent.getNextName()
                assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, currentUsername))
                assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
                    currentUsername,
                    roleName,
                ))
            }
        }
    }

    @TearDown(Level.Iteration)
    fun tearDownIteration() {
        println("tearDownTrial: concluded iteration number $iteration")
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
        iteration++
    }

    @Benchmark
    fun revokeUserFromRole() {
        assertSuccessOrThrow(
            coreRBACMQTTNoAC.revokeUserFromRole(
                username,
                roleName,
            )
        )
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 101, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeUserFromRoleIterateThroughParameter2 {

    private var username = "usernameUnderTest"
    private var roleName = "roleNameUnderTest"
    private var resourceNamesAlreadyPresent  = RandomNamesState()

    private var step = 5
    private var iteration = 0

    @Setup(Level.Trial)
    fun setUpTrial() {
        println("setUpTrial")
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, username))
        assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleName))
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        println("tearDownTrial")
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Setup(Level.Iteration)
    fun setUpIteration() {
        println("setUpIteration: started iteration number $iteration")
        if (iteration != 0) {
            for (i in 0 until step) {
                val currentResourceName = resourceNamesAlreadyPresent.getNextName().replace("+", "")
                assertSuccessOrThrow(
                    coreRBACMQTTNoAC.addResource(
                        currentResourceName,
                        "none".inputStream(),
                        COMBINED
                    )
                )
                assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
                    roleName,
                    currentResourceName,
                    PermissionType.READWRITE
                ))
            }
        }
    }

    @TearDown(Level.Iteration)
    fun tearDownIteration() {
        println("tearDownTrial: concluded iteration number $iteration")
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
        iteration++
    }

    @Benchmark
    fun revokeUserFromRole() {
        assertSuccessOrThrow(
            coreRBACMQTTNoAC.revokeUserFromRole(
                username,
                roleName,
            )
        )
    }
}



@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 101, time = 1, timeUnit = TimeUnit.SECONDS)
open class CoreRBACMQTTBenchmarkRevokeUserFromRoleIterateThroughParameter3 {

    private var username = "usernameUnderTest"
    private var roleName = "roleNameUnderTest"
    private var resourceName = "resourceNameUnderTest"
    private var roleNamesAlreadyPresent  = RandomNamesState()

    private var step = 5
    private var iteration = 0

    @Setup(Level.Trial)
    fun setUpTrial() {
        println("setUpTrial")
        coreRBACMQTTNoAC.configureServices()
        coreRBACMQTTNoAC.initCore()
        assertSuccessOrThrow(addAndInitUser(coreRBACMQTTNoAC, username))
        assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(roleName))
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
        assertSuccessOrThrow(
            coreRBACMQTTNoAC.addResource(
                resourceName,
                "none".inputStream(),
                COMBINED
            )
        )
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
            roleName,
            resourceName,
            PermissionType.READWRITE
        ))
    }

    @TearDown(Level.Trial)
    fun tearDownTrial() {
        println("tearDownTrial")
        coreRBACMQTTNoAC.subscribedTopicsKeysAndMessages.clear()
        TestUtilities.resetDMServiceRBACMQTT(coreRBACMQTTNoAC.dm)
        TestUtilities.resetMMServiceRBACRedis()
    }

    @Setup(Level.Iteration)
    fun setUpIteration() {
        println("setUpIteration: started iteration number $iteration")
        if (iteration != 0) {
            for (i in 0 until step) {
                val currentRoleName = roleNamesAlreadyPresent.getNextName()
                assertSuccessOrThrow(coreRBACMQTTNoAC.addRole(currentRoleName))
                assertSuccessOrThrow(coreRBACMQTTNoAC.assignPermissionToRole(
                    currentRoleName,
                    resourceName,
                    PermissionType.READWRITE
                ))
            }
        }
    }

    @TearDown(Level.Iteration)
    fun tearDownIteration() {
        println("tearDownTrial: concluded iteration number $iteration")
        assertSuccessOrThrow(coreRBACMQTTNoAC.assignUserToRole(
            username,
            roleName,
        ))
        iteration++
    }

    @Benchmark
    fun revokeUserFromRole() {
        assertSuccessOrThrow(
            coreRBACMQTTNoAC.revokeUserFromRole(
                username,
                roleName,
            )
        )
    }
}
