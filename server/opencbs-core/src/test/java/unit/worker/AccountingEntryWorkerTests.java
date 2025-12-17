package unit.worker;

import com.opencbs.core.accounting.services.AccountService;
import com.opencbs.core.accounting.services.AccountingEntryService;
import com.opencbs.core.officedocuments.services.PrintingFormService;
import com.opencbs.core.workers.AccountingEntryWorker;
import com.opencbs.core.workers.impl.AccountingEntryWorkerImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountingEntryWorkerTests {

    private AccountingEntryWorker accountingEntryWorker;

    @Mock
    private AccountingEntryService accountingEntryService;

    @Mock
    private AccountService accountService;

    @Mock
    private PrintingFormService printingFormService;

    @Before
    public void init() {
        accountingEntryWorker = new AccountingEntryWorkerImpl(
                accountingEntryService, accountService, printingFormService
        );
    }
}
