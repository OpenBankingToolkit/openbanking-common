import { Component, OnInit, ChangeDetectionStrategy, OnDestroy, ChangeDetectorRef, ViewChild } from '@angular/core';
import { MediaMatcher } from '@angular/cdk/layout';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';

import { SwaggerService } from '../../services/swagger.service';
import { MatSidenav } from '@angular/material/sidenav';
import { Subscription } from 'rxjs';
import { ISwaggerNavigation } from '../../models/swagger';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent implements OnInit, OnDestroy {
  mobileQuery: MediaQueryList;

  public data: any;
  public navigation: ISwaggerNavigation;
  private _mobileQueryListener: () => void;
  private _openedSubscription: Subscription;
  public isOpened = false;
  @ViewChild('sidenav', { static: true }) sidenav: MatSidenav;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private media: MediaMatcher,
    private swaggerService: SwaggerService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.mobileQuery = this.media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => this.changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit() {
    this.navigation = this.swaggerService.navigation;
    this._openedSubscription = this.sidenav.openedChange.subscribe(opened => {
      this.isOpened = opened;
      this.changeDetectorRef.detectChanges();
    });
    const path = this.route.snapshot.paramMap.get('path');
    this.data = this.swaggerService.getEndpointInfo(path);
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe((event: NavigationEnd) => {
      this.data = this.swaggerService.getEndpointInfo(event.url.substring(1));
      this.changeDetectorRef.markForCheck();
    });
  }

  toggle() {
    this.sidenav.toggle();
    this.changeDetectorRef.detectChanges();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
    this._openedSubscription.unsubscribe();
  }
}
