import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

@Component({
    selector: 'paginator-component',
    imports: [CommonModule, ButtonModule, SelectModule, FormsModule],
    templateUrl: './paginator.component.html',
    styleUrl: './paginator.component.css'
})
export class PaginatorComponent {

    @Output('paginationEvent') paginationEvent: EventEmitter<PaginationEvent> = new EventEmitter();

    @Input('pageNumber') pageNumber: number = 1;
    @Input('totalRegisters') totalRegisters: number = 1;
    @Input('itemsPerPage') itemsPerPage = PaginationEvent.DEFAULT_PAGE_SIZE;

    itemsPerPageList = [5, 10, 15, 25, 50];

    firstPage() {
        if (this.pageNumber === 1) {
            return;
        }
        
        this.pageNumber = 1;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    previousPage() {
        if (this.pageNumber === 1) {
            return;
        }

        this.pageNumber--;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    nextPage() {
        if (this.pageNumber === this.getTotalPages()) {
            return;
        }

        this.pageNumber++;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    lastPage() {
        if (this.pageNumber === this.getTotalPages()) {
            return;
        }

        this.pageNumber = this.getTotalPages();
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    getPaginationEvent(): PaginationEvent {
        return {
            pageNumber: this.pageNumber,
            totalPages: this.getTotalPages(),
            itemsPerPage: this.itemsPerPage,
        };
    }

    changeItemsPerPage(event: any) {
        console.log(event);
        this.itemsPerPage = event.value;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    getTotalPages() {
        return Math.ceil(this.totalRegisters / this.itemsPerPage);
    }

}

export class PaginationEvent {

    static readonly DEFAULT_PAGE_SIZE: number = 5;

    pageNumber: number;
    totalPages: number;
    itemsPerPage: number;

    constructor(pageNumber: number, totalPages: number, itemsPerPage: number) {
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.itemsPerPage = itemsPerPage;
    }
}
