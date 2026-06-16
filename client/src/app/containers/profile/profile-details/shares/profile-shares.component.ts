import { Component, OnDestroy, OnInit } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { environment } from '../../../../../environments/environment';
import { IProfile } from '../../../../core/store/profile/model/profile.model';
import { CurrentUserService } from '../../../../core/store/users/current-user/currentUser.service';
import { getProfileStatus } from '../../../../core/store/profile/profile.selectors';
import * as ProfileUtils from '../../shared/profile.utils';
import * as fromRoot from '../../../../core/core.reducer';
import { SharesService } from '../../../shares/shared/shares.service';
import { ShareLot } from '../../../shares/shared/shares.models';

@Component({
  standalone: false,
  selector: 'cbs-profile-shares',
  templateUrl: './profile-shares.component.html',
  styleUrls: ['./profile-shares.component.scss']
})
export class ProfileSharesComponent implements OnInit, OnDestroy {
  public profile: any;
  public navElements = [];
  public profileId: number;
  public profileType: string;
  public url: string;
  public imageUrl = '';
  public opened = false;
  public sharesPage: any;
  public portfolio: any;
  public isLoading = false;
  public queryObject = {
    page: 1,
    size: 20
  };

  private permissionSub: any;
  private statusSub: any;
  private routeSub: any;
  private profileSub: any;
  private permissions: any[];

  constructor(private profileStore$: Store<IProfile>,
              private route: ActivatedRoute,
              private sharesService: SharesService,
              private router: Router,
              private currentUserService: CurrentUserService) {
  }

  ngOnInit() {
    this.profile = this.profileStore$.pipe(select(fromRoot.getProfileState));

    this.permissionSub = this.currentUserService.currentUserPermissions$.subscribe(userPermissions => {
      this.permissions = userPermissions;
    });

    this.routeSub = this.route.parent.params.subscribe(params => {
      this.profileId = +params['id'];
      this.profileType = params['type'];
      this.queryObject = Object.assign({}, this.queryObject, { page: 1 });

      if (this.profileType === 'people' || this.profileType === 'companies') {
        this.url = `${environment.API_ENDPOINT}profiles/${this.profileType}/${this.profileId}/attachments`;
      }

      this.loadShares();
      this.loadPortfolio();
    });

    this.statusSub = this.profileStore$.select(fromRoot.getProfileState).pipe(getProfileStatus())
      .subscribe(() => {
        if (this.profileType === 'people' || this.profileType === 'companies') {
          this.navElements = ProfileUtils.setNavElements(
            this.profileType,
            this.profileId,
            this.permissions
          );
        }
      });
  }

  loadShares() {
    if (!this.profileId) {
      return;
    }
    this.isLoading = true;
    const params = Object.assign({}, this.queryObject, {
      page: this.queryObject.page - 1
    });
    this.sharesService.getMemberLots(this.profileId, params).subscribe((page: any) => {
      this.sharesPage = page;
      this.isLoading = false;
    }, () => {
      this.sharesPage = {
        content: [],
        totalElements: 0,
        size: this.queryObject.size
      };
      this.isLoading = false;
    });
  }

  loadPortfolio() {
    if (!this.profileId) {
      return;
    }
    this.sharesService.getMemberPortfolio(this.profileId).subscribe((portfolio: any) => {
      this.portfolio = portfolio;
    });
  }

  goToNextPage(page: number) {
    this.queryObject.page = page;
    const navigationExtras: NavigationExtras = {
      queryParams: this.queryObject
    };
    this.router.navigate([`profiles/${this.profileType}/${this.profileId}/shares`], navigationExtras);
    this.loadShares();
  }

  goToPurchase() {
    const navigationExtras: NavigationExtras = {
      queryParams: {
        profileId: this.profileId
      }
    };
    this.router.navigate(['/shares/purchase'], navigationExtras);
  }

  goToTransfer(lot: ShareLot) {
    const quantity = Math.min(lot.availableQuantity || 1, lot.quantity || lot.availableQuantity || 1);
    const navigationExtras: NavigationExtras = {
      queryParams: {
        sourceProfileId: this.profileId,
        shareProductId: lot.shareProduct?.id,
        lotId: lot.id,
        quantity,
        unitPrice: lot.unitPrice,
        transactionDate: new Date().toISOString().slice(0, 10),
        reason: `Transfer shares from lot ${lot.id}`
      }
    };
    this.router.navigate(['/shares/transfer'], navigationExtras);
  }

  openAttachment(attachment) {
    if (attachment.contentType && this.testIfImage(attachment.contentType)) {
      this.imageUrl = this.url + attachment.id;
      this.opened = true;
    } else {
      window.open(this.url + attachment.id);
    }
  }

  testIfImage(name: string) {
    const re = new RegExp(/^image/);
    return re.test(name);
  }

  getFileExtension(fileName: string) {
    return fileName.split('.').pop();
  }

  resetModal() {
    this.imageUrl = '';
  }

  ngOnDestroy() {
    this.permissionSub?.unsubscribe();
    this.routeSub.unsubscribe();
    this.statusSub.unsubscribe();
    if (this.profileSub) {
      this.profileSub.unsubscribe();
    }
  }
}
