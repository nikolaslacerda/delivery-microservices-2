import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {CuisineTypeService} from 'src/app/services/cuisine-type.service';
import {RestaurantService} from 'src/app/services/restaurant.service';
import {FormBuilder, Validators} from '@angular/forms';
import {RestaurantResponse} from '../../../models/response/restaurant.response.model';

@Component({
  selector: 'app-restaurant-list',
  templateUrl: './restaurant-list.component.html'
})
export class RestaurantListComponent implements OnInit {

  searchForm = this.fb.group({
    restaurantName: ['', Validators.required]
  });

  cep: string;
  cuisineTypes: Array<any>;
  mainCategory: string;
  q: string;
  allRestaurants: RestaurantResponse[] = [];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private cuisineTypeService: CuisineTypeService,
              private restaurantService: RestaurantService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.cep = this.route.snapshot.params.cep;

    this.cuisineTypeService.getCuisineTypes().subscribe(cuisineTypes => {
      this.cuisineTypes = cuisineTypes;
    });

    this.route.queryParams.subscribe(queryParams => {
      this.mainCategory = queryParams.mainCategory;
      this.getFeedRestaurants();
    });

    this.route.queryParams.subscribe(queryParams => {
      this.q = queryParams.q;
      this.getFeedRestaurants();
    });
  }

  getFeedRestaurants() {
    if (this.q) {
      this.restaurantService.getRestaurants(this.q).subscribe(restaurants => {
        this.allRestaurants = restaurants;
      });
    } else if (this.mainCategory) {
      this.restaurantService.getRestaurantsByCategory(this.mainCategory).subscribe(restaurants => {
        this.allRestaurants = restaurants;
      });
    } else {
      this.restaurantService.getRestaurants().subscribe(restaurants => {
        this.allRestaurants = restaurants;
      });
    }
  }

  onSubmit() {
    if (this.searchForm.valid) {
      this.restaurantService.getRestaurants(this.searchForm.value.restaurantName).subscribe(restaurants => {
        this.allRestaurants = restaurants;
      });
    } else {
      this.restaurantService.getRestaurants().subscribe(restaurants => {
        this.allRestaurants = restaurants;
      });
    }
  }
}