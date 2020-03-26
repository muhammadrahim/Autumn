import {Injectable, OnInit} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";

@Injectable()
export class DesignComponent implements OnInit {

  constructor(private httpClient: HttpClient, private router: Router, private cart: CartService) {
  }

  onSubmit() {
    this.httpClient.post(
      'http://localhost:8080/design',
      this.model, {
        headers: new HttpHeaders().set('Content-type', 'application/json'),
      }).subscribe(taco => this.cart.addToCart(taco));

    this.router.navigate(['/cart']);
  }

}
