import { Component, OnInit, Input, EventEmitter, Output, ViewChild, Inject } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { environment } from '../../../../environments/environment';
import { IPayeeItem } from '../../../core/store/loan-application';
import moment from 'moment';

import { FormLookupControlComponent } from '../../modules/cbs-form/components';

@Component({
  standalone: false,
  selector: 'cbs-payee-form-modal',
  templateUrl: 'payee-form-modal.component.html',
  styleUrls: ['payee-form-modal.component.scss']
})

export class PayeeFormModalComponent implements OnInit {
  @ViewChild('payee', { static: false }) payee: FormLookupControlComponent;
  public form: any;
  public payeeLookupUrl = {
    url: `${environment.API_ENDPOINT}payees/lookup`
  };
  public plannedDisbursementDate = moment().format(environment.DATE_FORMAT_MOMENT);

  constructor(
    public dialogRef: MatDialogRef<PayeeFormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { headerTitle: string }
  ) {}



  ngOnInit() {
    this.form = new FormGroup({
      id: new FormControl(''),
      payee: new FormControl('', Validators.required),
      payeeId: new FormControl('', Validators.required),
      plannedDisbursementDate: new FormControl('', Validators.required),
      amount: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    if (this.data && this.data.headerTitle) {
      // Handle dialog data if needed
    }
  }

  submit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  setPayeeDetails(payee) {
    this.form.controls['payee'].setValue(payee, { emitEvent: false });
    this.form.controls['plannedDisbursementDate'].setValue(this.plannedDisbursementDate);
  }

  setFormData(data: IPayeeItem) {
    if (data) {
      this.form.controls['id'].setValue(data.id);
      this.form.controls['payee'].setValue(data.payee);
      this.form.controls['payeeId'].setValue(data.payeeId);
      this.form.controls['plannedDisbursementDate'].setValue(data.plannedDisbursementDate);
      this.form.controls['amount'].setValue(data.amount);
      this.form.controls['description'].setValue(data.description);
    }
  }

  clearForm() {
    this.payee?.onClearLookup();
    this.form.reset();
  }
}
