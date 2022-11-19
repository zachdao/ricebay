import React, { useCallback, useState } from 'react';
import {
    Flex,
    Grid,
    Text,
    Button,
    TextField,
    View,
    ComboBox,
    Item,
    TextArea,
    Badge,
    ActionButton,
    DatePicker,
    RangeCalendar,
    LabeledValue,
} from '@adobe/react-spectrum';
import PublishCheck from '@spectrum-icons/workflow/PublishCheck';
import Image from '@spectrum-icons/workflow/Image';
import axios from 'axios';
import Close from '@spectrum-icons/workflow/Close';
import { CategoryTagGroup } from '../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import { getLocalTimeZone, today } from '@internationalized/date';

export const CreateAuction = () => {
    // Set up text field data
    const [image, setImage] = useState('');
    const [title, setTitle] = useState('');
    const [startingBid, setStartingBid] = useState('');
    const [bidIncrement, setBidIncrement] = useState('');
    const [description, setDescription] = useState('');
    let [range, setRange] = React.useState({
        start: today(getLocalTimeZone()),
        end: today(getLocalTimeZone()).add({ weeks: 1 }),
    });
    const [categories, setCategories] = useState(new Set([]));
    const [error, setError] = useState('');

    // Set up image field data

    // Set up category choice data

    // Set up date data
    const createAuction = useCallback(async () => {
        try {
            setError('');
            await axios.post('/auctions', {
                title,
                image,
                startingBid,
                bidIncrement,
                // startDate,
                // endDate,
                // categories,
                description,
            });
        } catch (e) {
            setError('Invalid attribute for auction!');
        }
    }, [
        title,
        image,
        startingBid,
        bidIncrement,
        // startDate,
        // endDate,
        // categories,
        description,
    ]);

    const isValid = useCallback(() => {
        return title.length > 0;
        //     // const validCategory = categories.length > 0;
        //     // const validStart = startDate > current time;
        //     // const validEnd = endDate > startDate;
        //     const validStartingBid = startingBid > 0;
        //     const validBidIncrement = bidIncrement > 0;
        //     const validDescription = description.length > 0;
    });

    return (
        <Grid
            margin="auto"
            marginTop="size-400"
            height="100%"
            width="50%"
            areas={['publish save', 'image details', 'description description']}
            columns={['3fr', '2fr']}
            rows={['2fr', '6fr', '3fr']}
            gap="size-150"
        >
            <Button
                variant="primary"
                gridArea="publish"
                width="size-1000"
                onPress={createAuction}
                isDisabled={!isValid()}
            >
                <PublishCheck />
                <Text>Publish</Text>
            </Button>
            <Flex
                direction="row"
                space="size-100"
                justifyContent="right"
                gridArea="save"
            >
                <Button variant="negative">
                    <Text>Cancel</Text>
                </Button>
                <Button variant="primary" marginStart="size-100">
                    <Text>Save</Text>
                </Button>
            </Flex>
            <ImageUploadWrapper onClick={() => console.log('click')}>
                <Image size="XXL" />
                <Text>Upload Image</Text>
            </ImageUploadWrapper>
            <Grid
                gridArea="details"
                areas={[
                    'title title',
                    'addcat cats',
                    'startdate enddate',
                    'startbid bidincrement',
                ]}
                gap="size-150"
                marginStart="size-200"
            >
                <TextField
                    label="Auction Title"
                    gridArea="title"
                    value={title}
                    onChange={setTitle}
                    width="100%"
                />
                <ComboBox
                    label="Add Category"
                    gridArea="addcat"
                    onSelectionChange={(selected) =>
                        setCategories((prev) => new Set([...prev, selected]))
                    }
                >
                    <Item key="red panda">Red Panda</Item>
                    <Item key="cat">Cat</Item>
                    <Item key="dog">Dog</Item>
                    <Item key="aardvark">Aardvark</Item>
                    <Item key="kangaroo">Kangaroo</Item>
                    <Item key="snake">Snake</Item>
                </ComboBox>
                <Flex direction="column" alignItems="start" gap="size-100">
                    <LabeledValue label="Categories" />
                    <CategoryTagGroup
                        categories={categories}
                        onRemove={(category) =>
                            setCategories(
                                (prev) =>
                                    new Set(
                                        Array.from(prev).filter(
                                            (el) => el !== category,
                                        ),
                                    ),
                            )
                        }
                    />
                </Flex>
                <Flex
                    direction="column"
                    gridColumnStart="startdate"
                    gridColumnEnd="enddate"
                    justifyContent="center"
                    alignItems="center"
                >
                    <LabeledValue label="Auction Dates" />
                    <RangeCalendar
                        aria-label="auction dates"
                        value={range}
                        onChange={setRange}
                    />
                </Flex>

                <TextField
                    label="Starting Bid"
                    gridArea="startbid"
                    value={startingBid}
                    onChange={setStartingBid}
                />
                <TextField
                    label="Minimum Bid Increment"
                    gridArea="bidincrement"
                    value={bidIncrement}
                    onChange={setBidIncrement}
                />
            </Grid>
            <TextArea
                label="Auction Description"
                gridArea="description"
                width="100%"
                height="size-3000"
                value={description}
                onChange={setDescription}
            />
        </Grid>
    );
};
const ImageUploadWrapper = styled.div`
    grid-area: image;
    width: 100%;
    height: 100%;
    border-width: 2px;
    border-color: grey;
    border-radius: 5px;
    border-style: solid;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
`;
