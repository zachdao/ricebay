import {
    Flex,
    Heading,
    Text,
    ListBox,
    Item,
    ComboBox,
} from '@adobe/react-spectrum';
import React, { useContext } from 'react';
import { AuctionList } from '../auction-list/AuctionList';
import { CategoriesContext } from '../categories.context';
import { SearchContext } from '../search.context';

export const Search = () => {
    const [category, setCategory] = React.useState(new Set([]));
    const [sort, setSort] = React.useState();
    const categoryOptions = useContext(CategoriesContext);
    const searchText = useContext(SearchContext);
    const sortOptions = [
        { name: 'Price Lowest to Highest' },
        { name: 'Price Highest to Lowest' },
        { name: 'Least Time Remaining' },
        { name: 'Most Time Remaining' },
    ];

    return (
        <Flex direction="row" alignItems="start" justifyContent="start">
            <Flex direction="column">
                <Heading level="1">Categories</Heading>
                <ListBox
                    selectionMode="multiple"
                    items={categoryOptions.map((opt) => ({ name: opt }))}
                    selectedKeys={category}
                    onSelectionChange={(selected) =>
                        setCategory(Array.from(selected))
                    }
                    width="size-2400"
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ListBox>
                <Heading level="1">Sort</Heading>
                <ComboBox
                    items={sortOptions}
                    selectedKeys={sort}
                    onSelectionChange={(selected) =>
                        setOptions(Array.from(selected))
                    }
                    width="size-2400"
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ComboBox>
            </Flex>

            {/*
            Using AuctionList as a placeholder
            Want to eventually return the auctions that the
            */}
            <AuctionList />
        </Flex>
    );
};
