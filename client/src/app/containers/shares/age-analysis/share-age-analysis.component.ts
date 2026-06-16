import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SharesService } from '../shared/shares.service';
import { ShareAgeAnalysis } from '../shared/shares.models';

@Component({
  standalone: false,
  selector: 'cbs-share-age-analysis',
  templateUrl: 'share-age-analysis.component.html',
  styleUrls: ['share-age-analysis.component.scss']
})
export class ShareAgeAnalysisComponent implements OnInit {

  public form: FormGroup;
  public analysis: ShareAgeAnalysis;
  public loading = false;

  constructor(private formBuilder: FormBuilder,
              private sharesService: SharesService) {
    this.form = this.formBuilder.group({
      reportDate: [new Date().toISOString().slice(0, 10)],
      shareProductId: [''],
      branchId: [''],
      profileId: ['']
    });
  }

  ngOnInit() {
    this.loadAnalysis();
  }

  loadAnalysis() {
    this.loading = true;
    this.sharesService.getAgeAnalysis(this.form.value).subscribe((analysis: ShareAgeAnalysis) => {
      this.analysis = analysis;
      this.loading = false;
    }, () => this.loading = false);
  }
}
