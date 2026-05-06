import { Component, Input, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  standalone: false,
  selector: 'cbs-payee-read-only',
  templateUrl: 'payee-read-only-modal.component.html',
  styles: [`
        .payee-readonly-form {
            display: flex;
            flex-direction: column;
            gap: 1rem;
            min-width: 400px;
        }

        .form-field {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .form-field label {
            font-weight: 500;
            color: rgba(0, 0, 0, 0.87);
        }

        .field-value {
            padding: 0.75rem;
            background-color: #f5f5f5;
            border-radius: 4px;
            border: 1px solid #e0e0e0;
        }

        .mat-mdc-dialog-content {
            padding: 1rem 0;
        }

        .mat-mdc-dialog-actions {
            padding: 1rem 0 0 0;
        }
    `]
})

export class PayeeReadOnlyComponent {
  public name: string;
  public disbursementDate: string;
  public amount: number;
  public description: string;

  constructor(
    public dialogRef: MatDialogRef<PayeeReadOnlyComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { headerTitle: string, payeeData: any }
  ) {
    if (this.data && this.data.payeeData) {
      this.setData(this.data.payeeData);
    }
  }

  setData(data) {
    this.name = data.payee.name;
    this.disbursementDate = data.disbursementDate;
    this.amount = data.amount;
    this.description = data.description;
  }

  close() {
    this.dialogRef.close();
  }
}
